package com.paytrack.expense.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.paytrack.expense.model.Category;

@FeignClient(name = "CATEGORY-SERVICE")
public interface CategoryClient {

    @GetMapping("/categories/internal/{username}")
    List<Category> getMyCategories(
            @PathVariable String username);
}