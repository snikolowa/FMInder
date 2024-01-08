package com.example.fminder.services;

import com.example.fminder.exceptions.BadRequestException;
import com.example.fminder.exceptions.NotFoundException;
import com.example.fminder.models.User;
import com.example.fminder.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User register(User registerUser) {

        if (userRepository.getUserByEmail(registerUser.getEmail()) != null) {
            throw new BadRequestException("There is already registered user with this email!");
        }

        userRepository.save(registerUser);
        User registeredUser = userRepository.getUserByEmail(registerUser.getEmail());
        if (registeredUser == null) {
            throw new NotFoundException("Unsuccessful registration.");
        }

        return registeredUser;
    }

    public User login(User loginUser) {
        User user = userRepository.getUserByEmail(loginUser.getEmail());

        if (user == null) {
            throw new NotFoundException("User not found!");
        }

        if (!Objects.equals(loginUser.getPassword(), user.getPassword())) {
            throw new BadRequestException("Wrong credentials!");
        }

        logger.info("User with id {} is logged.", loginUser.getId());

        return user;
    }
}
