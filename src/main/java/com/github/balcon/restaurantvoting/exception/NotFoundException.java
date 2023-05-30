package com.github.balcon.restaurantvoting.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends AppException {

    public static final String NOT_FOUND = "Not found";

    public NotFoundException(int id) {
        super(NOT_FOUND, HttpStatus.NOT_FOUND.value(), "Entity with id " + id + " not found");
    }
}