package com.paytrack.category.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paytrack.category.entity.Category;

public interface CategoryRepository 
        extends JpaRepository<Category, Long> {

    List<Category> findByUsername(String username);

    Optional<Category> findByNameAndUsername(
            String name,
            String username
    );
}