package com.example.fminder.services;

import com.example.fminder.models.Message;
import com.example.fminder.repositories.ChatRepository;
import com.example.fminder.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatRepository chatRepository;
    public List<Message> getChat(int currentUserId, int receiverId) {

        return chatRepository.getMessagesBetweenUsers(currentUserId, receiverId);
    }

    public Message saveMessage(Message message, int currentUserId, int receiverId) {
        message.setTimestamp(LocalDateTime.now());
        message.setSenderId(currentUserId);
        message.setReceiverId(receiverId);

        return chatRepository.save(message);
    }

    public List<Message> getLastMessagesWithEachUser(int currentUserId){
        return chatRepository.getLastMessagesWithEachUser(currentUserId);
    }
}
