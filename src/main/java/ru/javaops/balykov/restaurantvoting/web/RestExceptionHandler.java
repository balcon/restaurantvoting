package ru.javaops.balykov.restaurantvoting.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.javaops.balykov.restaurantvoting.exception.AppException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorInfo<?>> validationErrors(BindException e) {
        Map<String, String> errors = e.getFieldErrors().stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.joining(", "))));
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorInfo<>("Validation error", HttpStatus.UNPROCESSABLE_ENTITY.value(), errors));
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorInfo<?>> appErrors(AppException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode())
                .body(new ErrorInfo<>(e.getType(), e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo<?>> otherErrors(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorInfo<>("Server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }
}