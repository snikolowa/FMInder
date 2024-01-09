package com.example.fminder.models.DTOs;

import com.example.fminder.models.Request;
import com.example.fminder.models.User;

public class RequestUserDTO {
    private User user;
    private Request request;

    public RequestUserDTO(User user, Request request) {
        this.user = user;
        this.request = request;
    }

    public User getUser() {
        return user;
    }

    public Request getRequest() {
        return request;
    }
}
