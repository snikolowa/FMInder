package com.example.fminder.helpers;

import com.example.fminder.exceptions.AuthenticationFailureException;
import com.example.fminder.exceptions.EntityNotFoundException;
import com.example.fminder.models.User;
import com.example.fminder.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {

    public static final String AUTHENTICATION_FAILURE_MESSAGE = "Wrong email or password";
    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public static User tryGetUser(HttpSession session){
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null){
            throw new AuthenticationFailureException("The requested resource requires authentication");
        }
        return currentUser;
    }

    public User verifyAuthentication(String email, String password){
        try{
            User user = userService.getUserByEmail(email);
            if (!user.getPassword().equals(password)){
                throw new AuthenticationFailureException(AUTHENTICATION_FAILURE_MESSAGE);
            }
            return user;
        } catch (EntityNotFoundException ignored){
            throw new AuthenticationFailureException(AUTHENTICATION_FAILURE_MESSAGE);
        }
    }
}
