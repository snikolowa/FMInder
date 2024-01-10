package com.example.fminder.models;

import com.example.fminder.models.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
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
    @CreationTimestamp
    @Column(name = "created_on")
    private LocalDateTime timeCreated;
    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime timeUpdated;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;

}
