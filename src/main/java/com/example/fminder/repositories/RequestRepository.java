package com.example.fminder.repositories;

import com.example.fminder.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("SELECT DISTINCT r.senderId FROM Request r WHERE r.receiverId = :receiverUserId and r.status = 'Pending'")
    List<Long> findSenderIdsByReceiverUserId(int receiverUserId);

    @Query("SELECT r FROM Request r WHERE r.senderId = :senderId AND r.receiverId = :receiverId AND r.status != 'Denied'")
    Request findBySenderIdAndReceiverId(int senderId, int receiverId);

    @Query("SELECT r FROM Request r WHERE r.receiverId = :receiverUserId AND r.status = 'Pending'")
    List<Request> findAllPendingRequestsByReceiverUserId(int receiverUserId);

    @Query("SELECT DISTINCT \n" +
            "    CASE \n" +
            "        WHEN r.receiverId = :currentUserId THEN r.senderId\n" +
            "        WHEN r.senderId = :currentUserId THEN r.receiverId\n" +
            "    END AS userId\n" +
            "FROM \n" +
            "    Request r\n" +
            "WHERE \n" +
            "    (r.receiverId = :currentUserId OR r.senderId = :currentUserId) \n" +
            "    AND (r.status = 'Accepted' OR r.status = 'Pending')")
    List<Long> getUserIdsOfAcceptedOrPendingRequest(int currentUserId);
}
