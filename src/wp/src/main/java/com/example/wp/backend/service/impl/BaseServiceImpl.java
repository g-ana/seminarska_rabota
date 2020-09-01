package com.example.wp.backend.service.impl;

import com.example.wbs.ConsumingLinkedData;
import com.example.wp.backend.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BaseServiceImpl implements BaseService {

    private ConsumingLinkedData consumingLinkedData;

    public BaseServiceImpl(ConsumingLinkedData consumingLinkedData) {
        this.consumingLinkedData = consumingLinkedData;
    }

    public List<Map<String, String>> findAllLandmarks() {
        return this.consumingLinkedData.findAllLandmarks();
    }

    public Map<String, String> findLandmarkByName(String name) {
        return this.consumingLinkedData.findLandmarkByName(name);
    }

    public List<Map<String, String>> findLandmarksPaged(int page, int pageSize) {
        return this.consumingLinkedData.findLandmarksPaged(page, pageSize);
    }

}
