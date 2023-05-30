package com.github.balcon.restaurantvoting.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalTime;

@UtilityClass
public class DateTimeUtil {
    public LocalDate currentDate() {
        return LocalDate.now();
    }

    public LocalTime currentTime() {
        return LocalTime.now();
    }
}