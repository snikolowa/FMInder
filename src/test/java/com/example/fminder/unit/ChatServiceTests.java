package com.example.fminder.unit;

import com.example.fminder.models.Message;
import com.example.fminder.repositories.ChatRepository;
import com.example.fminder.services.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ChatServiceTests {

    @Mock
    private ChatRepository chatRepository;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getChat_shouldReturnMessages() {
        // Arrange
        int currentUserId = 1;
        int receiverId = 2;
        List<Message> expectedMessages = Arrays.asList(new Message(), new Message());
        when(chatRepository.getMessagesBetweenUsers(currentUserId, receiverId)).thenReturn(expectedMessages);

        // Act
        List<Message> result = chatService.getChat(currentUserId, receiverId);

        // Assert
        assertEquals(result.size(), 2);
        assertEquals(expectedMessages, result);
        verify(chatRepository, times(1)).getMessagesBetweenUsers(currentUserId, receiverId);
    }

    @Test
    void saveMessage_shouldSaveAndReturnMessage() {
        // Arrange
        int currentUserId = 1;
        int receiverId = 2;
        Message message = new Message();
        message.setMessage("Hello");

        when(chatRepository.save(message)).thenReturn(message);

        // Act
        Message result = chatService.saveMessage(message, currentUserId, receiverId);

        // Assert
        assertEquals(message, result);
        assertEquals(currentUserId, message.getSenderId());
        assertEquals(receiverId, message.getReceiverId());
        assertNotNull(message.getTimestamp());
        verify(chatRepository, times(1)).save(message);
    }
}

