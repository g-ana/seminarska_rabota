package com.example.wp.backend.web.controllers;

import org.springframework.web.bind.annotation.*;
import com.example.wp.backend.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/home")
public class BaseController {

    private final BaseServiceImpl baseService;

    public BaseController(BaseServiceImpl baseService) {
        this.baseService = baseService;
    }

    @GetMapping("/landmarks/all")
    public List<Map<String, String>> findAllLandmarks() {
        return this.baseService.findAllLandmarks();
    }

    @GetMapping("/landmarks/{name}")
    public Map<String, String> findLandmarkByName(@PathVariable String name) {
        return this.baseService.findLandmarkByName(name);
    }
    @GetMapping("/landmarks")
    public List<Map<String, String>> findLandmarksPaged(
            @RequestHeader(name="page", required = false, defaultValue = "0") int page,
            @RequestHeader(name="page-size", required = false, defaultValue = "2") int pageSize) {
        return this.baseService.findLandmarksPaged(page, pageSize);
    }
}
