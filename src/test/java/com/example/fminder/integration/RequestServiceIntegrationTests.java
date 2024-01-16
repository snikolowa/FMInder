package com.example.fminder.integration;

import com.example.fminder.models.DTOs.RequestUserDTO;
import com.example.fminder.models.Request;
import com.example.fminder.models.User;
import com.example.fminder.models.enums.RequestStatus;
import com.example.fminder.repositories.RequestRepository;
import com.example.fminder.repositories.UserRepository;
import com.example.fminder.services.RequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
@Sql(scripts = "classpath:insert-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RequestServiceIntegrationTests {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    RequestService requestService;

    @Autowired
    private UserRepository userRepository;


    @Test
    void createRequest() {
        // Arrange
        List<User> users = userRepository.findAll().stream().limit(2).toList();
        int receiverId = users.get(0).getId();
        int senderId = users.get(1).getId();

        // Act
        Request result = requestService.create(senderId, receiverId);

        // Assert
        assert result.getSenderId() == senderId;
        assert result.getReceiverId() == receiverId;
        assert result.getStatus() == RequestStatus.Pending;
    }

    @Test
    void getRequests() {
        // Arrange
        int userId = userRepository.getUserByEmail("user2@example.com").getId();

        // Act
        List<RequestUserDTO> requests = requestService.getRequests(userId);

        // Assert
        assert requests.size() == 1;
        assert requests.get(0).getUser().getId() == 5;
        assert requests.get(0).getRequest().getStatus() == RequestStatus.Pending;
    }

    @Test
    void updateRequest() {
        // Arrange
        int requestId = 1;
        int receiverId = 1;
        String status = "Accepted";

        // Act
        Request request = requestService.update(requestId, status, receiverId);

        // Assert
        assert request.getStatus() == RequestStatus.Accepted;

    }
}

