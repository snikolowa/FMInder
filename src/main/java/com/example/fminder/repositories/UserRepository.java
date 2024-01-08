package com.example.fminder.repositories;

import com.example.fminder.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserById(int id);

    User getUserByEmail(String email);

    List<User> findAllByIdIsNot(int userId);

}

