package com.example.fminder.controllers;

import com.example.fminder.exceptions.UnauthorizedException;
import com.example.fminder.helpers.AuthenticationHelper;
import com.example.fminder.models.DTOs.CreateRequestDto;
import com.example.fminder.models.Request;
import com.example.fminder.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/requests")
    public ResponseEntity<Request> createRequest(@Valid @RequestBody CreateRequestDto createRequestDto, HttpServletRequest request) {
        int currentUserId = authenticationHelper.getLoggedUserId(request);
        return new ResponseEntity<>(requestService.create(currentUserId, createRequestDto.getReceiverUserId()), HttpStatus.CREATED);
    }
}
