package com.paytrack.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paytrack.auth.entity.Role;
import com.paytrack.auth.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>{

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    long countByActiveTrue();

    long countByActiveFalse();

    long countByRole(Role role);

}