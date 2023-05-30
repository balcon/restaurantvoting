package com.github.balcon.restaurantvoting.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.github.balcon.restaurantvoting.exception.AppException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorInfo<Map<String, String>>> validationErrors(BindException e) {
        Map<String, String> errors = e.getFieldErrors().stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.joining(", "))));
        log.error(e.getMessage(), e);
        return ResponseEntity.unprocessableEntity()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorInfo<>("Validation error", HttpStatus.UNPROCESSABLE_ENTITY.value(), errors));
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorInfo<?>> appErrors(AppException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorInfo<>(e.getType(), e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorInfo<String>> typeMismatch(MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.unprocessableEntity()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorInfo<>("Type mismatch", HttpStatus.UNPROCESSABLE_ENTITY.value(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo<String>> otherErrors(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.internalServerError()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorInfo<>("Server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }
}