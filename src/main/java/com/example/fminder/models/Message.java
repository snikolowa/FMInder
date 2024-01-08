package com.example.fminder.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "chats")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private int id;

    @Column(name = "receiver_id")
    private int receiverId;

    @Column(name = "sender_id")
    private int senderId;

    @Column(name = "message")
    private String message;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

}
