package com.example.fminder.services;

import com.example.fminder.exceptions.BadRequestException;
import com.example.fminder.exceptions.NotFoundException;
import com.example.fminder.models.Request;
import com.example.fminder.models.User;
import com.example.fminder.models.enums.RequestStatus;
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

    public Request create(int currentUserId, int receiverUserId) {
        User receiver = userRepository.getUserById(receiverUserId);
        if (receiver == null) {
            throw new NotFoundException("Receiver user of request not found!");
        }

        Request request = requestRepository.findBySenderIdAndReceiverId(currentUserId, receiverUserId);
        if (request != null) {
            if (request.getStatus() == RequestStatus.Pending) {
                throw new BadRequestException("Request already send!");
            } else if (request.getStatus() == RequestStatus.Accepted) {
                throw new BadRequestException("You are already a match!");
            }
        }

        Request newRequest = new Request();
        newRequest.setSenderId(currentUserId);
        newRequest.setReceiverId(receiverUserId);
        newRequest.setStatus(RequestStatus.Pending);

        // Save the new Request entity
        return requestRepository.save(newRequest);
    }
}
