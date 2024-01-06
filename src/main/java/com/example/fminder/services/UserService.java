package com.example.fminder.services;

import com.example.fminder.exceptions.NotFoundException;
import com.example.fminder.models.User;
import com.example.fminder.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(int id){
        User user = userRepository.getUserById(id);

        if (user == null){
            throw new NotFoundException("User not found!");
        }

        return user;
    }

    public User getUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email);

        if (user == null){
            throw new NotFoundException("User not found!");
        }

        return user;
    }

    public User updateUser(int userId, User updatedUser) {
        User existingUser = userRepository.getUserById(userId);

        if (existingUser == null) {
            throw new NotFoundException("User not found!");
        }
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setGender(updatedUser.getGender());
            existingUser.setMajor(updatedUser.getMajor());
            existingUser.setGraduateYear(updatedUser.getGraduateYear());
            existingUser.setInterests(updatedUser.getInterests());

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
}
