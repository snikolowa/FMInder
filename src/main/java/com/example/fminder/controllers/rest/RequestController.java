package com.example.fminder.controllers.rest;

import com.example.fminder.controllers.rest.BaseController;
import com.example.fminder.exceptions.BadRequestException;
import com.example.fminder.exceptions.UnauthorizedException;
import com.example.fminder.models.DTOs.CreateRequestDto;
import com.example.fminder.models.DTOs.RequestUserDTO;
import com.example.fminder.models.Request;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RequestController extends BaseController {

    @GetMapping("/users/{id}/requests")
    public ResponseEntity<List<RequestUserDTO>> getRequestOfUserById(@PathVariable int id, HttpServletRequest request){
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

    @PatchMapping("/requests/{id}/{status}")
    public ResponseEntity<Request> updateRequestStatus(@PathVariable long id, @PathVariable String status, HttpServletRequest request){
        int userId = authenticationHelper.getLoggedUserId(request);
        if (!status.equals("Accepted") && !status.equals("Denied")) {
            throw new BadRequestException("Status can accept only values - Accepted and Denied!");
        }

        return new ResponseEntity<>(requestService.update(id, status, userId), HttpStatus.OK);
    }
}
