package com.example.fminder.models;

import com.example.fminder.models.enums.RequestStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private int id;
    @Column(name = "sender_user_id")
    private int senderId;
    @Column(name = "receiver_user_id")
    private int receiverId;

    @Column(name = "created_on")
    private LocalDateTime timeCreated;

    @Column(name = "updated_on")
    private LocalDateTime timeUpdated;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;

    public Request() {
    }

    public Request(int id, int senderId, int receiverId, LocalDateTime timeCreated, LocalDateTime timeUpdated, RequestStatus status) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public LocalDateTime getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(LocalDateTime timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
