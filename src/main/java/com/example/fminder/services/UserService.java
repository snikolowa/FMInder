package com.example.fminder.services;

import com.example.fminder.exceptions.NotFoundException;
import com.example.fminder.models.Request;
import com.example.fminder.models.User;
import com.example.fminder.repositories.RequestRepository;
import com.example.fminder.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RequestRepository requestRepository;

    public static final String INVALID_FORMAT_OF_THE_PICTURE = "Invalid format of the picture";

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, RequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
    }

    public User getUserById(int id) {
        User user = userRepository.getUserById(id);

        if (user == null) {
            throw new NotFoundException("User not found!");
        }

        return user;
    }

    public User getUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email);

        if (user == null) {
            throw new NotFoundException("User not found!");
        }

        return user;
    }

    public User updateUser(int userId, User updatedUser) {
        User existingUser = userRepository.getUserById(userId);

        if (existingUser == null) {
            throw new NotFoundException("User not found!");
        }
        if (updatedUser.getFirstName() != null) {
            existingUser.setFirstName(updatedUser.getFirstName());
        }

        if (updatedUser.getLastName() != null) {
            existingUser.setLastName(updatedUser.getLastName());
        }

        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getGender() != null) {
            existingUser.setGender(updatedUser.getGender());
        }

        if (updatedUser.getMajor() != null) {
            existingUser.setMajor(updatedUser.getMajor());
        }

        if (updatedUser.getGraduateYear() != 0) {
            existingUser.setGraduateYear(updatedUser.getGraduateYear());
        }

        if (updatedUser.getInterests() != null) {
            existingUser.setInterests(updatedUser.getInterests());
        }


        return userRepository.save(existingUser);
    }

    public User changePassword(int userId, User user) {
        User existingUser = userRepository.getUserById(userId);

        if (existingUser == null) {
            throw new NotFoundException("User not found!");
        }

        existingUser.setPassword(user.getPassword());
        return userRepository.save(existingUser);
    }

    public List<User> getPotentialMatches(int userId) {
        User currentUser = userRepository.getUserById(userId);
        List<User> potentialMatches = userRepository.findAllByIdIsNot(userId);

        potentialMatches.sort(Comparator.comparingInt(user ->
                Math.abs(user.getGraduateYear() - currentUser.getGraduateYear())));

        return potentialMatches;
    }

    @Async
    public CompletableFuture<String> uploadProfilePicture(MultipartFile file, int userId) {
        User user = userRepository.getUserById(userId);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (!fileName.endsWith(".jpeg") && !fileName.endsWith(".jpg") && !fileName.endsWith(".png")) {
            throw new IllegalArgumentException(INVALID_FORMAT_OF_THE_PICTURE);
        }
        try {
            user.setProfilePicture(Base64.getEncoder().encodeToString(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        userRepository.save(user);
        return CompletableFuture.completedFuture(fileName);
    }

    public List<User> getMatches(int userId) {
        List<Long> userIds = userRepository.findSenderIdsByReceiverUserId(userId);
        List<User> matches = new ArrayList<>();

        for (Long id: userIds){
            matches.add(userRepository.getUserById(Math.toIntExact(id)));
        }
        return matches;
    }
}
