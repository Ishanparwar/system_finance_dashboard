package com.zorvyn.financeSystem.repository;

import com.zorvyn.financeSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // would be using it for the auth purpose later
}
