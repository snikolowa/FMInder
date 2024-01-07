package com.example.fminder.controllers;

import com.example.fminder.exceptions.NotFoundException;
import com.example.fminder.exceptions.UnauthorizedException;
import com.example.fminder.exceptions.BadRequestException;
import com.example.fminder.helpers.AuthenticationHelper;
import com.example.fminder.services.AuthenticationService;
import com.example.fminder.services.RequestService;
import com.example.fminder.services.UserService;
import com.example.fminder.helpers.ErrorObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {

    @Autowired
    public UserService userService;

    @Autowired
    public AuthenticationService authenticationService;

    @Autowired
    public AuthenticationHelper authenticationHelper;

    @Autowired
    public RequestService requestService;

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorObj handleBadRequest(Exception e) {
        return createError(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorObj handleNotFound(Exception e) {
        return createError(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorObj handleUnauthorized(Exception e) {
        return createError(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorObj handleOtherExceptions(Exception e) {
        return createError(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorObj createError(Exception e, HttpStatus status) {
        e.printStackTrace();
        ErrorObj error = new ErrorObj();
        error.setMessage(e.getMessage());
        error.setDataAndTime(LocalDateTime.now());
        error.setStatus(status.value());
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}