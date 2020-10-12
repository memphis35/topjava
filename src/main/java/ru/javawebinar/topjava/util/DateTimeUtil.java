package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T> boolean isBetweenHalfOpen(Comparable<T> lt, T startTime, T endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalTime convertToTime(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public static LocalDate convertToDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}

