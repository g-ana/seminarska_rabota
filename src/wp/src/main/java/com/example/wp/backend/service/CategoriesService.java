package com.example.wp.backend.service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Map;

public interface CategoriesService {
    public List<Map<String, String>> findAllCategories();
    public Map<String, String> findCategoryByName(String name);
    public List<Map<String, String>> findCategoriesPaged(int page, int pageSize);
}
