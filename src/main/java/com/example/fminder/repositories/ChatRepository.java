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

    @Query("SELECT m " +
            "FROM Message m " +
            "WHERE (m.senderId = :currentUserId OR m.receiverId = :currentUserId) " +
            "    AND m.timestamp = (SELECT MAX(m2.timestamp) " +
            "                      FROM Message m2 " +
            "                      WHERE (m2.senderId = m.senderId AND m2.receiverId = m.receiverId) " +
            "                         OR (m2.senderId = m.receiverId AND m2.receiverId = m.senderId))")
    List<Message> getLastMessagesWithEachUser(int currentUserId);

}
