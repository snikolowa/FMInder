package com.example.fminder.repositories;

import com.example.fminder.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ChatRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m " +
            "WHERE (m.senderId = :currentUserId AND m.receiverId = :receiverId) " +
            "OR (m.senderId = :receiverId AND m.receiverId = :currentUserId)")
    List<Message> getMessagesBetweenUsers(int currentUserId, int receiverId);
}