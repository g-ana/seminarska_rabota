package com.example.wp.backend.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidTouristAttractionNameException extends RuntimeException {

    public InvalidTouristAttractionNameException(String touristAttraction) {
        super("Tourist attraction " + touristAttraction + " is not found! ");
    }
}
