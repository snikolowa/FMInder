package com.example.fminder.repositories;

import com.example.fminder.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("SELECT DISTINCT r.senderId FROM Request r WHERE r.receiverId = :receiverUserId and r.status = 'Pending'")
    List<Long> findReceiverIdsBySenderUserId(int receiverUserId);

    @Query("SELECT r FROM Request r WHERE r.senderId = :senderId AND r.receiverId = :receiverId AND r.status != 'Denied'")
    Request findBySenderIdAndReceiverId(int senderId, int receiverId);
}
