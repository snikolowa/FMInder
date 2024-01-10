package com.example.fminder.services;

import com.example.fminder.exceptions.BadRequestException;
import com.example.fminder.exceptions.NotFoundException;
import com.example.fminder.exceptions.UnauthorizedException;
import com.example.fminder.models.DTOs.RequestUserDTO;
import com.example.fminder.models.Request;
import com.example.fminder.models.User;
import com.example.fminder.models.enums.RequestStatus;
import com.example.fminder.repositories.RequestRepository;
import com.example.fminder.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    private final RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public List<RequestUserDTO> getRequests(int id) {
        // TODO return request ids too
        List<Long> userIds = requestRepository.findSenderIdsByReceiverUserId(id);
        List<User> users = userRepository.findAllById(userIds);
        List<Request> requests = requestRepository.findAllPendingRequestsByReceiverUserId(id);

        List<RequestUserDTO> result = new ArrayList<>();
        for (Request request : requests) {
            User matchingUser = users.stream()
                    .filter(user -> user.getId() == request.getSenderId())
                    .findFirst()
                    .orElse(null);

            if (matchingUser != null) {
                result.add(new RequestUserDTO(matchingUser, request));
            }
        }

        return result;
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

    public Request update(long id, String status, int userId) {
        Optional<Request> optionalRequest = requestRepository.findById(id);
        Request request = optionalRequest.orElseThrow(() -> new NotFoundException("Request not found!"));
        if (request.getReceiverId() != userId) {
            throw new UnauthorizedException("Unauthorized action!");
        }

        request.setStatus(RequestStatus.valueOf(status));
        return requestRepository.save(request);

    }
}
