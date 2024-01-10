//package com.example.fminder;
//
//import com.example.fminder.exceptions.BadRequestException;
//import com.example.fminder.exceptions.NotFoundException;
//import com.example.fminder.exceptions.UnauthorizedException;
//import com.example.fminder.models.DTOs.RequestUserDTO;
//import com.example.fminder.models.Request;
//import com.example.fminder.models.User;
//import com.example.fminder.models.enums.RequestStatus;
//import com.example.fminder.repositories.RequestRepository;
//import com.example.fminder.repositories.UserRepository;
//import com.example.fminder.services.RequestService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class RequestServiceTests {
//
//    @Mock
//    private RequestRepository requestRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private RequestService requestService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void getRequests_shouldReturnRequestUserDTOList() {
//        // Arrange
//        int receiverUserId = 1;
//
//        List<Long> senderIds = Arrays.asList(2L, 3L);
//        User user1 = new User();
//        user1.setId(2);
//        user1.setFirstName("User1");
//        user1.setLastName("LastName1");
//        User user2 = new User();
//        user2.setId(3);
//        user2.setFirstName("User2");
//        user2.setLastName("LastName2");
//        List<User> users = Arrays.asList(user1, user2);
//
//        Request request1 = new Request();
//        request1.setId(1);
//        request1.setSenderId(2);
//        request1.setReceiverId(receiverUserId);
//        Request request2 = new Request();
//        request2.setId(2);
//        request2.setSenderId(3);
//        request2.setReceiverId(receiverUserId);
//        List<Request> requests = Arrays.asList(request1, request2);
//
//        when(requestRepository.findSenderIdsByReceiverUserId(receiverUserId)).thenReturn(senderIds);
//        when(userRepository.findAllById(senderIds)).thenReturn(users);
//        when(requestRepository.findAllPendingRequestsByReceiverUserId(receiverUserId)).thenReturn(requests);
//
//        // Act
//        List<RequestUserDTO> result = requestService.getRequests(receiverUserId);
//
//        // Assert
//        assertEquals(2, result.size());
//        assertEquals("User2", result.get(0).getUser().getFirstName());
//        assertEquals("LastName2", result.get(0).getUser().getLastName());
//        assertEquals(1L, result.get(0).getRequest().getId());
//        assertEquals("User3", result.get(1).getUser().getFirstName());
//        assertEquals("LastName3", result.get(1).getUser().getLastName());
//        assertEquals(2L, result.get(1).getRequest().getId());
//
//        // Verify that repository methods were called
//        verify(requestRepository, times(1)).findSenderIdsByReceiverUserId(receiverUserId);
//        verify(userRepository, times(1)).findAllById(senderIds);
//        verify(requestRepository, times(1)).findAllPendingRequestsByReceiverUserId(receiverUserId);
//    }
//
//    @Test
//    void getRequests_noRequestsFound_shouldReturnEmptyList() {
//        // Arrange
//        int receiverUserId = 1;
//
//        when(requestRepository.findSenderIdsByReceiverUserId(receiverUserId)).thenReturn(Arrays.asList());
//        when(userRepository.findAllById(Arrays.asList())).thenReturn(Arrays.asList());
//        when(requestRepository.findAllPendingRequestsByReceiverUserId(receiverUserId)).thenReturn(Arrays.asList());
//
//        // Act
//        List<RequestUserDTO> result = requestService.getRequests(receiverUserId);
//
//        // Assert
//        assertEquals(0, result.size());
//
//        // Verify that repository methods were called
//        verify(requestRepository, times(1)).findSenderIdsByReceiverUserId(receiverUserId);
//        verify(userRepository, never()).findAllById(anyList());
//        verify(requestRepository, never()).findAllPendingRequestsByReceiverUserId(receiverUserId);
//    }
//
//    @Test
//    void create_shouldCreateAndReturnRequest() {
//        // Arrange
//        int currentUserId = 1;
//        int receiverUserId = 2;
//        User receiverUser = new User();
//        receiverUser.setId(receiverUserId);
//
//        when(userRepository.getUserById(receiverUserId)).thenReturn(receiverUser);
//        when(requestRepository.findBySenderIdAndReceiverId(currentUserId, receiverUserId)).thenReturn(null);
//        when(requestRepository.save(any(Request.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // Act
//        Request result = requestService.create(currentUserId, receiverUserId);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(currentUserId, result.getSenderId());
//        assertEquals(receiverUserId, result.getReceiverId());
//        assertEquals(RequestStatus.Pending, result.getStatus());
//        verify(userRepository, times(1)).getUserById(receiverUserId);
//        verify(requestRepository, times(1)).findBySenderIdAndReceiverId(currentUserId, receiverUserId);
//        verify(requestRepository, times(1)).save(any(Request.class));
//    }
//
//    @Test
//    void create_existingPendingRequest_shouldThrowBadRequestException() {
//        // Arrange
//        int currentUserId = 1;
//        int receiverUserId = 2;
//        User receiverUser = new User();
//        receiverUser.setId(receiverUserId);
//
//        Request existingRequest = new Request();
//        existingRequest.setStatus(RequestStatus.Pending);
//
//        when(userRepository.getUserById(receiverUserId)).thenReturn(receiverUser);
//        when(requestRepository.findBySenderIdAndReceiverId(currentUserId, receiverUserId)).thenReturn(existingRequest);
//
//        // Act & Assert
//        assertThrows(BadRequestException.class, () -> requestService.create(currentUserId, receiverUserId));
//        verify(userRepository, times(1)).getUserById(receiverUserId);
//        verify(requestRepository, times(1)).findBySenderIdAndReceiverId(currentUserId, receiverUserId);
//        verify(requestRepository, never()).save(any(Request.class));
//    }
//
//    @Test
//    void create_existingAcceptedRequest_shouldThrowBadRequestException() {
//        // Arrange
//        int currentUserId = 1;
//        int receiverUserId = 2;
//        User receiverUser = new User();
//        receiverUser.setId(receiverUserId);
//
//        Request existingRequest = new Request();
//        existingRequest.setStatus(RequestStatus.Accepted);
//
//        when(userRepository.getUserById(receiverUserId)).thenReturn(receiverUser);
//        when(requestRepository.findBySenderIdAndReceiverId(currentUserId, receiverUserId)).thenReturn(existingRequest);
//
//        // Act & Assert
//        assertThrows(BadRequestException.class, () -> requestService.create(currentUserId, receiverUserId));
//        verify(userRepository, times(1)).getUserById(receiverUserId);
//        verify(requestRepository, times(1)).findBySenderIdAndReceiverId(currentUserId, receiverUserId);
//        verify(requestRepository, never()).save(any(Request.class));
//    }
//
//    @Test
//    void update_validRequest_shouldUpdateAndReturnRequest() {
//        // Arrange
//        int requestId = 1;
//        String status = "Accepted";
//        int userId = 2;
//
//        Request existingRequest = new Request();
//        existingRequest.setId(requestId);
//        existingRequest.setReceiverId(userId);
//        existingRequest.setStatus(RequestStatus.Pending);
//
//        when(requestRepository.findById((long) requestId)).thenReturn(Optional.of(existingRequest));
//        when(requestRepository.save(any(Request.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // Act
//        Request result = requestService.update(requestId, status, userId);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(RequestStatus.Accepted, result.getStatus());
//        verify(requestRepository, times(1)).findById((long) requestId);
//        verify(requestRepository, times(1)).save(any(Request.class));
//    }
//
//    @Test
//    void update_shouldThrowNotFoundException() {
//        // Arrange
//        long nonExistingRequestId = 999L;
//        String status = "Accepted";
//        int userId = 2;
//
//        when(requestRepository.findById(nonExistingRequestId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(NotFoundException.class, () -> requestService.update(nonExistingRequestId, status, userId));
//        verify(requestRepository, times(1)).findById(nonExistingRequestId);
//        verify(requestRepository, never()).save(any(Request.class));
//    }
//
//    @Test
//    void update_shouldThrowUnauthorizedException() {
//        // Arrange
//        int requestId = 1;
//        String status = "Accepted";
//        int unauthorizedUserId = 999;
//        int requestReceiverId = 2;
//
//        Request existingRequest = new Request();
//        existingRequest.setId(requestId);
//        existingRequest.setReceiverId(requestReceiverId);
//        existingRequest.setStatus(RequestStatus.Pending);
//
//        when(requestRepository.findById((long) requestId)).thenReturn(Optional.of(existingRequest));
//
//        // Act & Assert
//        assertThrows(UnauthorizedException.class, () -> requestService.update(requestId, status, unauthorizedUserId));
//        verify(requestRepository, times(1)).findById((long) requestId);
//        verify(requestRepository, never()).save(any(Request.class));
//    }
//
//}
