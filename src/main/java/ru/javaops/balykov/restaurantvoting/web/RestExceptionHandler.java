package ru.javaops.balykov.restaurantvoting.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationErrors(MethodArgumentNotValidException e) {
        Map<String, List<String>> fieldErrors = e.getFieldErrors().stream()
                .collect(Collectors.groupingBy(FieldError::getField, // TODO: 09.04.2023 make it better, NestedExceptionUtils?
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));
        return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
    }
    // TODO: 13.04.2023 User email unique
    // TODO: 13.04.2023 Restaurants name and address unique 
}