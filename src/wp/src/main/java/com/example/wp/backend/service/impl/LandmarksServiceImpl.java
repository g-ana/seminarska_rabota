package com.example.wp.backend.service.impl;

import com.example.wp.backend.service.LandmarksService;
import org.springframework.stereotype.Service;
import com.example.wbs.ConsumingLinkedData;

@Service
public class LandmarksServiceImpl implements LandmarksService {

    private final ConsumingLinkedData consumingLinkedData;

    public LandmarksServiceImpl(ConsumingLinkedData consumingLinkedData) {
        this.consumingLinkedData=consumingLinkedData;
    }


}
