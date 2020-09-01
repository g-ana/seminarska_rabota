package com.example.wp.backend.web.controllers;

import com.example.wp.backend.service.impl.LandmarksServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/landmarks")
public class LandmarksController {

    private final LandmarksServiceImpl landmarksService;

    public LandmarksController(LandmarksServiceImpl landmarksService) {
        this.landmarksService=landmarksService;
    }
}
