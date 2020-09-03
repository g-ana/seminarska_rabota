package com.example.wp.backend.web.rest_controllers;

import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import com.example.wp.backend.service.impl.CategoriesServiceImpl;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/categories", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
@CrossOrigin(origins="http://localhost:3000")
public class CategoriesController {

    private final CategoriesServiceImpl categoriesService;

    public CategoriesController(CategoriesServiceImpl categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping("/all")
    public List<Map<String, String>> findAllCategories() {
        return this.categoriesService.findAllCategories();
    }

    @GetMapping("/{name}")
    public Map<String, String> findCategoryByName(@PathVariable String name) {
        return this.categoriesService.findCategoryByName(name);
    }
    
    @GetMapping
    public List<Map<String, String>> findCategoriesPaged(
            @RequestHeader(name="page", required = false, defaultValue = "0") int page,
            @RequestHeader(name="page-size", required = false, defaultValue = "2") int pageSize) {
        return this.categoriesService.findCategoriesPaged(page, pageSize);
    }
}
