package com.example.wp.backend.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidLandmarkNameException extends RuntimeException {

    public InvalidLandmarkNameException(String landmark) {
        super("Landmark " + landmark + " is not found! ");
    }
}
