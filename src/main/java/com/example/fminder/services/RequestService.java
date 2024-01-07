package com.example.fminder.services;

import com.example.fminder.models.User;
import com.example.fminder.repositories.RequestRepository;
import com.example.fminder.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    private final RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public List<User> getRequests(int id) {
        List<Long> userIds = requestRepository.findReceiverIdsBySenderUserId(id);
        List<User> users = userRepository.findAllById(userIds);

        return users;
    }
}
