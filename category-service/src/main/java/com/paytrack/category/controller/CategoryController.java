package com.paytrack.category.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.paytrack.category.entity.Category;
import com.paytrack.category.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private static final Logger logger =
            LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService service;

    @PostMapping
    public Category addCategory(
            @Valid @RequestBody Category category,
            Principal principal){

        String username = principal.getName();

        logger.info("Adding category for user: {}", username);

        category.setUsername(username);

        return service.addCategory(category);
    }

    @GetMapping("/my")
    public List<Category> getMyCategories(
            Principal principal){

        String username = principal.getName();

        logger.info("Fetching categories for user: {}", username);

        return service.getMyCategories(username);
    }
 
    @GetMapping
    public List<Category> getAll(){
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            Principal principal){

        service.delete(id, principal.getName());
    }

    @GetMapping("/internal/{username}")
    public List<Category> getInternal(
            @PathVariable String username){

        return service.getMyCategories(username);
    }
}