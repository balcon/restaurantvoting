package ru.javaops.balykov.restaurantvoting.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalTime;

@UtilityClass
public class DateTimeUtil {
    public final LocalTime REVOTE_DEADLINE = LocalTime.of(11, 0);

    public LocalDate currentDate() {
        return LocalDate.now();
    }

    public LocalTime currentTime() {
        return LocalTime.now();
    }
}