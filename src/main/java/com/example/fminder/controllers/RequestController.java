package com.example.fminder.controllers;

import com.example.fminder.exceptions.UnauthorizedException;
import com.example.fminder.models.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RequestController extends BaseController {

    @GetMapping("/users/{id}/requests")
    public ResponseEntity<List<User>> getRequestOfUserById(@PathVariable int id, HttpServletRequest request){
        int userId = authenticationHelper.getLoggedUserId(request);
        if (userId != id) {
            throw new UnauthorizedException("You don't have access to this resource!");
        }

        return new ResponseEntity<>(requestService.getRequests(id), HttpStatus.OK);
    }
}
