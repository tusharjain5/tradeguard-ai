package com.tradeguard.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tradeguard.entity.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationCode(String code);
    boolean existsByEmail(String email);
}