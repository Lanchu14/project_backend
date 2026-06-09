package com.paytrack.category.repository;

import com.paytrack.category.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    @Test
    void testFindByUsername(){

        Category category =
                Category.builder()
                        .name("Food")
                        .type("EXPENSE")
                        .username("user")
                        .build();

        repository.save(category);

        List<Category> list =
                repository.findByUsername("user");

        assertFalse(list.isEmpty());
    }

    @Test
    void testFindByNameAndUsername(){

        Category category =
                Category.builder()
                        .name("Food")
                        .type("EXPENSE")
                        .username("user")
                        .build();

        repository.save(category);

        var result =
                repository.findByNameAndUsername("Food","user");

        assertTrue(result.isPresent());
    }
}