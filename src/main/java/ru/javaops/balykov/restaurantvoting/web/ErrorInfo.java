package ru.javaops.balykov.restaurantvoting.web;

public record ErrorInfo<T>(String type, int code, T details) {
}