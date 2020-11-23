package ru.javawebinar.topjava.util;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomDateTimeFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<CustomDateTime> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(List.of(LocalDate.class, LocalTime.class));
    }

    @Override
    public Printer<?> getPrinter(CustomDateTime annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    @Override
    public Parser<?> getParser(CustomDateTime annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    private Formatter<?> configureFormatterFrom(CustomDateTime annotation, Class<?> fieldType) {
        CustomDateTime.Type type = annotation.type();
        boolean isMax = annotation.maxValue();
        System.out.println(fieldType.getCanonicalName());
        switch (type) {
            case DATE: {
                CustomDateFormatter formatter = new CustomDateFormatter();
                formatter.setMaxValue(isMax);
                return formatter;
            }
            case TIME:
                CustomTimeFormatter formatter = new CustomTimeFormatter();
                formatter.setTimeIsMin(isMax);
                return formatter;
            default:
                throw new IllegalArgumentException(type.name());
        }
    }
}
