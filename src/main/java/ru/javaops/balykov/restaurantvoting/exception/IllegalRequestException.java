package ru.javaops.balykov.restaurantvoting.exception;

import org.springframework.http.HttpStatus;

public class IllegalRequestException extends AppException {

    public static final String ILLEGAL_REQUEST = "Illegal Request";

    public IllegalRequestException(String message) {
        super(ILLEGAL_REQUEST, HttpStatus.UNPROCESSABLE_ENTITY.value(), message);
    }
}