package com.github.balcon.restaurantvoting.web;

public record ErrorInfo<T>(String type, int code, T details) {
}