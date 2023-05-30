package com.github.balcon.restaurantvoting.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class AppException extends RuntimeException {
    private final String type;
    private final int errorCode;
    private final String message;
}