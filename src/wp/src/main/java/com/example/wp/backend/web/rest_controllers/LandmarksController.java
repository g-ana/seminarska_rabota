package com.example.wp.backend.web.rest_controllers;

import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.wp.backend.service.impl.LandmarksServiceImpl;

@RestController
@RequestMapping(value = "/api/landmarks", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
@CrossOrigin("http://localhost:3000")
public class LandmarksController {

    private final LandmarksServiceImpl landmarksService;

    public LandmarksController(LandmarksServiceImpl landmarksService) {
        this.landmarksService=landmarksService;
    }
}
