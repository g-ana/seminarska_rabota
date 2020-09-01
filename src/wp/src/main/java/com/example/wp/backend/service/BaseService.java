package com.example.wp.backend.service;

import java.util.List;
import java.util.Map;

public interface BaseService {
    public List<Map<String, String>> findAllLandmarks();
    public Map<String, String> findLandmarkByName(String name);
    public List<Map<String, String>> findLandmarksPaged(int page, int pageSize);
}
