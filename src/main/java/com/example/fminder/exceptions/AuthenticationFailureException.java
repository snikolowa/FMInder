package com.example.fminder.exceptions;

import com.example.fminder.helpers.AuthenticationHelper;

public class AuthenticationFailureException extends RuntimeException{
    public AuthenticationFailureException(String message){ super(message);}
}
