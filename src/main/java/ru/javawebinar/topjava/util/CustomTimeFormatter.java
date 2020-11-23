package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomTimeFormatter implements Formatter<LocalTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private boolean isMaxValue = false;

    @Override
    public LocalTime parse(String time, Locale locale) throws ParseException {
        return !time.isEmpty() ? LocalTime.from(formatter.parse(time)) :
                isMaxValue ? LocalTime.of(23, 59, 59) : LocalTime.of(0,0, 0);
    }

    @Override
    public String print(LocalTime date, Locale locale) {
        return "LocalTime value: " + date.toString();
    }

    public void setTimeIsMin(boolean isMax) {
        isMaxValue = isMax;
    }
}
