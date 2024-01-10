package com.example.fminder;

import com.example.fminder.exceptions.BadRequestException;
import com.example.fminder.exceptions.NotFoundException;
import com.example.fminder.exceptions.UnauthorizedException;
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
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRequests_shouldReturnUsers() {
        // Arrange
        int userId = 1;
        List<Long> receiverIds = Arrays.asList(2L, 3L);
        List<User> expectedUsers = Arrays.asList(new User(), new User());

        when(requestRepository.findReceiverIdsBySenderUserId(userId)).thenReturn(receiverIds);
        when(userRepository.findAllById(receiverIds)).thenReturn(expectedUsers);

        // Act
        List<User> result = requestService.getRequests(userId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedUsers, result);
        verify(requestRepository, times(1)).findReceiverIdsBySenderUserId(userId);
        verify(userRepository, times(1)).findAllById(receiverIds);
    }

    @Test
    void create_shouldCreateAndReturnRequest() {
        // Arrange
        int currentUserId = 1;
        int receiverUserId = 2;
        User receiverUser = new User();
        receiverUser.setId(receiverUserId);

        when(userRepository.getUserById(receiverUserId)).thenReturn(receiverUser);
        when(requestRepository.findBySenderIdAndReceiverId(currentUserId, receiverUserId)).thenReturn(null);
        when(requestRepository.save(any(Request.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Request result = requestService.create(currentUserId, receiverUserId);

        // Assert
        assertNotNull(result);
        assertEquals(currentUserId, result.getSenderId());
        assertEquals(receiverUserId, result.getReceiverId());
        assertEquals(RequestStatus.Pending, result.getStatus());
        verify(userRepository, times(1)).getUserById(receiverUserId);
        verify(requestRepository, times(1)).findBySenderIdAndReceiverId(currentUserId, receiverUserId);
        verify(requestRepository, times(1)).save(any(Request.class));
    }

    @Test
    void create_existingPendingRequest_shouldThrowBadRequestException() {
        // Arrange
        int currentUserId = 1;
        int receiverUserId = 2;
        User receiverUser = new User();
        receiverUser.setId(receiverUserId);

        Request existingRequest = new Request();
        existingRequest.setStatus(RequestStatus.Pending);

        when(userRepository.getUserById(receiverUserId)).thenReturn(receiverUser);
        when(requestRepository.findBySenderIdAndReceiverId(currentUserId, receiverUserId)).thenReturn(existingRequest);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> requestService.create(currentUserId, receiverUserId));
        verify(userRepository, times(1)).getUserById(receiverUserId);
        verify(requestRepository, times(1)).findBySenderIdAndReceiverId(currentUserId, receiverUserId);
        verify(requestRepository, never()).save(any(Request.class));
    }

    @Test
    void create_existingAcceptedRequest_shouldThrowBadRequestException() {
        // Arrange
        int currentUserId = 1;
        int receiverUserId = 2;
        User receiverUser = new User();
        receiverUser.setId(receiverUserId);

        Request existingRequest = new Request();
        existingRequest.setStatus(RequestStatus.Accepted);

        when(userRepository.getUserById(receiverUserId)).thenReturn(receiverUser);
        when(requestRepository.findBySenderIdAndReceiverId(currentUserId, receiverUserId)).thenReturn(existingRequest);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> requestService.create(currentUserId, receiverUserId));
        verify(userRepository, times(1)).getUserById(receiverUserId);
        verify(requestRepository, times(1)).findBySenderIdAndReceiverId(currentUserId, receiverUserId);
        verify(requestRepository, never()).save(any(Request.class));
    }

    @Test
    void update_validRequest_shouldUpdateAndReturnRequest() {
        // Arrange
        int requestId = 1;
        String status = "Accepted";
        int userId = 2;

        Request existingRequest = new Request();
        existingRequest.setId(requestId);
        existingRequest.setReceiverId(userId);
        existingRequest.setStatus(RequestStatus.Pending);

        when(requestRepository.findById((long) requestId)).thenReturn(Optional.of(existingRequest));
        when(requestRepository.save(any(Request.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Request result = requestService.update(requestId, status, userId);

        // Assert
        assertNotNull(result);
        assertEquals(RequestStatus.Accepted, result.getStatus());
        verify(requestRepository, times(1)).findById((long) requestId);
        verify(requestRepository, times(1)).save(any(Request.class));
    }

    @Test
    void update_shouldThrowNotFoundException() {
        // Arrange
        long nonExistingRequestId = 999L;
        String status = "Accepted";
        int userId = 2;

        when(requestRepository.findById(nonExistingRequestId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> requestService.update(nonExistingRequestId, status, userId));
        verify(requestRepository, times(1)).findById(nonExistingRequestId);
        verify(requestRepository, never()).save(any(Request.class));
    }

    @Test
    void update_shouldThrowUnauthorizedException() {
        // Arrange
        int requestId = 1;
        String status = "Accepted";
        int unauthorizedUserId = 999;
        int requestReceiverId = 2;

        Request existingRequest = new Request();
        existingRequest.setId(requestId);
        existingRequest.setReceiverId(requestReceiverId);
        existingRequest.setStatus(RequestStatus.Pending);

        when(requestRepository.findById((long) requestId)).thenReturn(Optional.of(existingRequest));

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> requestService.update(requestId, status, unauthorizedUserId));
        verify(requestRepository, times(1)).findById((long) requestId);
        verify(requestRepository, never()).save(any(Request.class));
    }

}
