package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomDateFormatter implements Formatter<LocalDate> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private boolean isMaxValue = false;

    @Override
    public LocalDate parse(String date, Locale locale) throws ParseException {
        System.out.println(date);
        return !date.isEmpty() ? LocalDate.from(formatter.parse(date)) :
                isMaxValue ? LocalDate.of(2050, 1, 1) : LocalDate.of(1970, 1, 1);
    }

    @Override
    public String print(LocalDate date, Locale locale) {
        return "LocalDate value: " + date.toString();
    }

    public void setMaxValue(boolean maxValue) {
        this.isMaxValue = maxValue;
    }
}
