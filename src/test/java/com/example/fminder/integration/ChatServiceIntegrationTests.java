package com.example.fminder.integration;

import com.example.fminder.models.Message;
import com.example.fminder.repositories.ChatRepository;
import com.example.fminder.services.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Objects;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
@Sql(scripts = "classpath:insert-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ChatServiceIntegrationTests {

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    ChatService chatService;

    @Test
    void getChat() {
        // Arrange
        int currentUserId = 1;
        int receiverUserId = 2;

        // Act
        List<Message> chat = chatService.getChat(currentUserId, receiverUserId);

        // Assert
        assert chat.size() == 4;
        assert chat.stream().allMatch(it -> it.getSenderId() == currentUserId || it.getReceiverId() == currentUserId);
        assert chat.stream().allMatch(it -> it.getSenderId() == receiverUserId || it.getReceiverId() == receiverUserId);
    }

    @Test
    void saveMessage() {
        // Arrange
        int currentUserId = 3;
        int receiverUserId = 5;
        Message messageToSend = new Message();
        messageToSend.setMessage("testMessage");

        // Act
        Message message = chatService.saveMessage(messageToSend, currentUserId, receiverUserId);

        // Assert
        assert message.getReceiverId() == receiverUserId;
        assert message.getSenderId() == currentUserId;
        assert Objects.equals(message.getMessage(), "testMessage");
    }

    @Test
    void getLastMessagesWithEachUser() {
        // Arrange
        int userId = 1;

        // Act
        List<Message> messages = chatService.getLastMessagesWithEachUser(userId);

        // Assert
        assert messages.size() == 3;
        assert messages.stream().allMatch(it -> it.getSenderId() == userId || it.getReceiverId() == userId);
    }

}
