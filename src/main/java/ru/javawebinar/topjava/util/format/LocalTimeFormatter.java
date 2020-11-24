package ru.javawebinar.topjava.util.format;

import org.springframework.format.Formatter;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.Locale;

public class LocalTimeFormatter implements Formatter<LocalTime> {
    @Override
    public LocalTime parse(String time, Locale locale) throws ParseException {
        return DateTimeUtil.parseLocalTime(time);
    }

    @Override
    public String print(LocalTime date, Locale locale) {
        return "LocalTime value: " + date.toString();
    }
}
