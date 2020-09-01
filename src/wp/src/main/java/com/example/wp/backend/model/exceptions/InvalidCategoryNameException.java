package com.example.wp.backend.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidCategoryNameException extends RuntimeException {

    public InvalidCategoryNameException(String category) {
        super("Category " + category + " is not found! ");
    }
}
