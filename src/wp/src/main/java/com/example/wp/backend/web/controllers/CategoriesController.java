package com.example.wp.backend.web.controllers;

import org.springframework.web.bind.annotation.RequestHeader;
import com.example.wp.backend.service.impl.CategoriesServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/categories")
public class CategoriesController {

    private final CategoriesServiceImpl categoriesService;

    public CategoriesController(CategoriesServiceImpl categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping
    public List<Map<String, String>> findCategoriesPaged(
            @RequestHeader(name="page", required = false, defaultValue = "0") int page,
            @RequestHeader(name = "page-size", required = false, defaultValue = "2") int pageSize) {
        return this.categoriesService.findCategoriesPaged(page, pageSize);
    }

    @GetMapping("/all")
    public List<Map<String, String>> findAllCategories() {
        return this.categoriesService.findAllCategories();
    }

    @GetMapping("/{name}")
    public Map<String, String> findCategoryByName(@PathVariable String name) {
        return this.categoriesService.findCategoryByName(name);
    }
}
