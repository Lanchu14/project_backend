package com.paytrack.category.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.paytrack.category.entity.Category;
import com.paytrack.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger =
            LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository repository;

    @Override
    public Category addCategory(Category category) {

        repository.findByNameAndUsername(
                category.getName(),
                category.getUsername()
        ).ifPresent(c -> {
            throw new IllegalArgumentException(
                    "Category already exists"
            );
        });

        return repository.save(category);
    }

    @Override
    public List<Category> getMyCategories(String username) {

        logger.info("Fetching categories for: {}", username);

        return repository.findByUsername(username);
    }

    @Override
    public List<Category> getAll() {

        logger.info("Fetching all categories");

        return repository.findAll();
    }

    @Override
    public void delete(Long id, String username) {

        Category category =
                repository.findById(id)
                .orElseThrow();

        if(!category.getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }

        repository.deleteById(id);
    }
}