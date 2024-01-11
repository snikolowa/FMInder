package com.example.fminder.controllers.rest;

import com.example.fminder.exceptions.BadRequestException;
import com.example.fminder.helpers.AuthenticationHelper;
import com.example.fminder.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController extends BaseController {

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User registerUser) {
        registerUser.setEmail(registerUser.getEmail().trim());
        registerUser.setPassword(registerUser.getPassword().trim());

        return new ResponseEntity<>(authenticationService.register(registerUser), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User loginUser, HttpServletRequest request) {
        if (request.getSession().getAttribute(AuthenticationHelper.LOGGED) != null && (boolean) request.getSession().getAttribute(AuthenticationHelper.LOGGED)) {
            throw new BadRequestException("You are already logged in");
        }
        if (loginUser.getEmail() == null || loginUser.getPassword() == null) {
            throw new BadRequestException("Email and password are required fields!");
        }

        loginUser.setEmail(loginUser.getEmail().trim());
        loginUser.setPassword(loginUser.getPassword().trim());

        User resultUser = authenticationService.login(loginUser);
        authenticationHelper.loginUser(request, resultUser.getId());
        return new ResponseEntity<>(resultUser, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>("Logged out", HttpStatus.OK);
    }

}
