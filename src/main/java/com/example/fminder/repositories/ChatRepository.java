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

    @Query("SELECT m FROM Message m " +
            "WHERE (m.senderId = :currentUserId OR m.receiverId = :currentUserId) " +
            "AND (m.senderId, m.receiverId, m.timestamp) IN " +
            "(SELECT m2.senderId, m2.receiverId, MAX(m2.timestamp) " +
            "FROM Message m2 " +
            "WHERE (m2.senderId = :currentUserId OR m2.receiverId = :currentUserId) " +
            "GROUP BY m2.senderId, m2.receiverId) " +
            "ORDER BY m.timestamp DESC")
    List<Message> getLastMessagesWithEachUser(int currentUserId);
}