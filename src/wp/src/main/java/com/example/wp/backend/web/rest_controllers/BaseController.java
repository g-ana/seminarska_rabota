package com.example.wp.backend.web.rest_controllers;

import com.example.wp.backend.service.impl.BaseServiceImpl;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/home", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
@CrossOrigin(origins="http://localhost:3000")
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
