package com.example.fminder.unit;

import com.example.fminder.exceptions.BadRequestException;
import com.example.fminder.exceptions.NotFoundException;
import com.example.fminder.exceptions.UnauthorizedException;
import com.example.fminder.models.DTOs.RequestUserDTO;
import com.example.fminder.models.Request;
import com.example.fminder.models.User;
import com.example.fminder.models.enums.RequestStatus;
import com.example.fminder.repositories.RequestRepository;
import com.example.fminder.repositories.UserRepository;
import com.example.fminder.services.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestServiceTests {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RequestService requestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getRequests_shouldReturnRequestUserDTOList() {
        // Arrange
        int receiverUserId = 1;

        List<Long> senderIds = Arrays.asList(2L, 3L);
        User user2 = new User();
        user2.setId(2);
        user2.setFirstName("User2");
        user2.setLastName("LastName2");
        User user3 = new User();
        user3.setId(3);
        user3.setFirstName("User3");
        user3.setLastName("LastName3");
        List<User> users = Arrays.asList(user2, user3);
        Request request1 = new Request();
        request1.setId(1);
        request1.setSenderId(2);
        request1.setReceiverId(receiverUserId);
        Request request2 = new Request();
        request2.setId(2);
        request2.setSenderId(3);
        request2.setReceiverId(receiverUserId);

        List<Request> requests = Arrays.asList(request1, request2);

        when(requestRepository.findSenderIdsByReceiverUserId(receiverUserId)).thenReturn(senderIds);
        when(userRepository.findAllById(senderIds)).thenReturn(users);
        when(requestRepository.findAllPendingRequestsByReceiverUserId(receiverUserId)).thenReturn(requests);

        // Act
        List<RequestUserDTO> result = requestService.getRequests(receiverUserId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("User2", result.get(0).getUser().getFirstName());
        assertEquals("LastName2", result.get(0).getUser().getLastName());
        assertEquals(1L, result.get(0).getRequest().getId());
        assertEquals("User3", result.get(1).getUser().getFirstName());
        assertEquals("LastName3", result.get(1).getUser().getLastName());
        assertEquals(2L, result.get(1).getRequest().getId());

        // Verify that repository methods were called
        verify(requestRepository, times(1)).findSenderIdsByReceiverUserId(receiverUserId);
        verify(userRepository, times(1)).findAllById(senderIds);
        verify(requestRepository, times(1)).findAllPendingRequestsByReceiverUserId(receiverUserId);
    }

    @Test
    void create_shouldCreateRequest() {
        // Arrange
        int currentUserId = 1;
        int receiverUserId = 2;
        User receiver = new User();
        receiver.setId(receiverUserId);
        receiver.setFirstName("Receiver");
        receiver.setLastName("User");
        Request request = new Request();
        request.setSenderId(currentUserId);
        request.setReceiverId(receiverUserId);
        request.setStatus(RequestStatus.Pending);

        when(userRepository.getUserById(receiverUserId)).thenReturn(receiver);
        when(requestRepository.findBySenderIdAndReceiverId(currentUserId, receiverUserId)).thenReturn(null);
        when(requestRepository.save(request)).thenReturn(request);

        // Act
        Request result = requestService.create(currentUserId, receiverUserId);

        // Assert
        assertNotNull(result);
        assertEquals(currentUserId, result.getSenderId());
        assertEquals(receiverUserId, result.getReceiverId());
        assertEquals(RequestStatus.Pending, result.getStatus());

        // Verify that repository methods were called
        verify(userRepository, times(1)).getUserById(receiverUserId);
        verify(requestRepository, times(1)).findBySenderIdAndReceiverId(currentUserId, receiverUserId);
        verify(requestRepository, times(1)).save(any(Request.class));
    }

    @Test
    void create_requestAlreadySent_shouldThrowBadRequestException() {
        // Arrange
        int currentUserId = 1;
        int receiverUserId = 2;
        Request existingRequest = new Request();
        existingRequest.setId(1);
        existingRequest.setSenderId(currentUserId);
        existingRequest.setReceiverId(receiverUserId);
        existingRequest.setStatus(RequestStatus.Pending);

        when(userRepository.getUserById(receiverUserId)).thenReturn(new User());
        when(requestRepository.findBySenderIdAndReceiverId(currentUserId, receiverUserId)).thenReturn(existingRequest);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            requestService.create(currentUserId, receiverUserId);
        });

        assertEquals("Request already send!", exception.getMessage());

        // Verify that repository methods were called
        verify(userRepository, times(1)).getUserById(receiverUserId);
        verify(requestRepository, times(1)).findBySenderIdAndReceiverId(currentUserId, receiverUserId);
        verify(requestRepository, never()).save(any(Request.class));
    }

    @Test
    void create_alreadyAMatch_shouldThrowBadRequestException() {
        // Arrange
        int currentUserId = 1;
        int receiverUserId = 2;
        Request existingRequest = new Request();
        existingRequest.setId(1);
        existingRequest.setSenderId(currentUserId);
        existingRequest.setReceiverId(receiverUserId);
        existingRequest.setStatus(RequestStatus.Accepted);

        when(userRepository.getUserById(receiverUserId)).thenReturn(new User());
        when(requestRepository.findBySenderIdAndReceiverId(currentUserId, receiverUserId)).thenReturn(existingRequest);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            requestService.create(currentUserId, receiverUserId);
        });

        assertEquals("You are already a match!", exception.getMessage());

        // Verify that repository methods were called
        verify(userRepository, times(1)).getUserById(receiverUserId);
        verify(requestRepository, times(1)).findBySenderIdAndReceiverId(currentUserId, receiverUserId);
        verify(requestRepository, never()).save(any(Request.class));
    }

    @Test
    void update_shouldUpdateRequestStatus() {
        // Arrange
        long requestId = 1L;
        String status = "Accepted";
        int userId = 2;
        Request existingRequest = new Request();
        existingRequest.setId((int) requestId);
        existingRequest.setSenderId(1);
        existingRequest.setReceiverId(userId);
        existingRequest.setStatus(RequestStatus.Pending);

        when(requestRepository.findById(requestId)).thenReturn(Optional.of(existingRequest));
        when(requestRepository.save(existingRequest)).thenReturn(existingRequest);

        // Act
        Request result = requestService.update(requestId, status, userId);

        // Assert
        assertNotNull(result);
        assertEquals(RequestStatus.Accepted, result.getStatus());

        // Verify that repository methods were called
        verify(requestRepository, times(1)).findById(requestId);
        verify(requestRepository, times(1)).save(existingRequest);
    }

    @Test
    void update_requestNotFound_shouldThrowNotFoundException() {
        // Arrange
        long requestId = 1L;
        String status = "Accepted";
        int userId = 2;

        when(requestRepository.findById(requestId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            requestService.update(requestId, status, userId);
        });

        assertEquals("Request not found!", exception.getMessage());

        // Verify that repository methods were called
        verify(requestRepository, times(1)).findById(requestId);
        verify(requestRepository, never()).save(any(Request.class));
    }

    @Test
    void update_unauthorizedAction_shouldThrowUnauthorizedException() {
        // Arrange
        long requestId = 1L;
        String status = "Accepted";
        int userId = 2;
        Request existingRequest = new Request();
        existingRequest.setId((int) requestId);
        existingRequest.setSenderId(1);
        existingRequest.setReceiverId(3);
        existingRequest.setStatus(RequestStatus.Pending);

        when(requestRepository.findById(requestId)).thenReturn(Optional.of(existingRequest));

        // Act & Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            requestService.update(requestId, status, userId);
        });

        assertEquals("Unauthorized action!", exception.getMessage());

        // Verify that repository methods were called
        verify(requestRepository, times(1)).findById(requestId);
        verify(requestRepository, never()).save(any(Request.class));
    }

}


