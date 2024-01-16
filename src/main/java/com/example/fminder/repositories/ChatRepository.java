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

    @Query("SELECT c FROM Message c JOIN (SELECT CASE WHEN c.senderId = :currentUserId THEN c.receiverId WHEN c.receiverId = :currentUserId THEN c.senderId END AS otherUser, MAX(c.timestamp) AS maxTimestamp FROM Message c  WHERE c.receiverId = :currentUserId OR c.senderId = :currentUserId GROUP BY otherUser) lastMsg ON ((c.senderId = :currentUserId AND c.receiverId = lastMsg.otherUser) OR (c.receiverId = :currentUserId AND c.senderId = lastMsg.otherUser)) AND c.timestamp = lastMsg.maxTimestamp")
    List<Message> getLastMessagesWithEachUser(int currentUserId);
}
