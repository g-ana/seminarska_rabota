package com.example.wp.backend.service.impl;

import com.example.wp.backend.service.CategoriesService;
import org.springframework.stereotype.Service;
import com.example.wbs.ConsumingLinkedData;

import java.util.List;
import java.util.Map;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    private final ConsumingLinkedData consumingLinkedData;

    public CategoriesServiceImpl(ConsumingLinkedData consumingLinkedData) {
        this.consumingLinkedData = consumingLinkedData;
    }

    public List<Map<String, String>> findAllCategories() {
        return this.consumingLinkedData.findAllCategories();
    }

    public Map<String, String> findCategoryByName(String name) {
        return this.consumingLinkedData.findCategoryByName(name);
    }

    public List<Map<String, String>> findCategoriesPaged(int page, int pageSize) {
        return this.consumingLinkedData.findCategoriesPaged(page, pageSize);
    }

}
