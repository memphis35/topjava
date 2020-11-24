package ru.javawebinar.topjava.util.format;

import org.springframework.format.Formatter;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {
    @Override
    public LocalDate parse(String date, Locale locale) throws ParseException {
        return DateTimeUtil.parseLocalDate(date);
    }

    @Override
    public String print(LocalDate date, Locale locale) {
        return "LocalDate value: " + date.toString();
    }
}
