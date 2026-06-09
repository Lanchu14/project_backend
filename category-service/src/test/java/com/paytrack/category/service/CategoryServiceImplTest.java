package com.paytrack.category.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.paytrack.category.entity.Category;
import com.paytrack.category.repository.CategoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryServiceImpl service;

    private Category category;

    @BeforeEach
    void setup() {

        category = Category.builder()
                
                .name("Food")
                .type("Expense")
                .username("testuser")
                .build();
    }

    @Test
    void addCategory() {

        when(repository.save(any()))
                .thenReturn(category);

        Category result = service.addCategory(category);

        assertEquals("Food", result.getName());
    }

    @Test
    void getMyCategories() {

        when(repository.findByUsername(any()))
                .thenReturn(List.of(category));

        List<Category> result =
                service.getMyCategories("testuser");

        assertEquals(1, result.size());
    }

    @Test
    void getAll() {

        when(repository.findAll())
                .thenReturn(List.of(category));

        List<Category> result = service.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void delete() {

        when(repository.findById(any()))
                .thenReturn(Optional.of(category));

        service.delete(1L, "testuser");

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteUnauthorized() {

        category.setUsername("other");

        when(repository.findById(any()))
                .thenReturn(Optional.of(category));

        assertThrows(RuntimeException.class,
                () -> service.delete(1L, "testuser"));
    }
}