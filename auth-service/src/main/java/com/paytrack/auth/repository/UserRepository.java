package com.paytrack.auth.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paytrack.auth.entity.Role;
import com.paytrack.auth.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    long countByActiveTrue();

    long countByActiveFalse();

    long countByRole(Role role);

    List<User> findByActiveTrue();

    List<User> findByActiveFalse();

    // Uncomment later when enabling 30-day rule
    List<User> findByLastLoginBefore(LocalDateTime date);
}