package com.paytrack.category.service;

import java.util.List;

import com.paytrack.category.entity.Category;

public interface CategoryService {

    Category addCategory(Category category);

    List<Category> getMyCategories(String username);

    List<Category> getAll();

    void delete(Long id, String username);
}