package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MealTo {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final long id;

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final boolean excess;

    public MealTo(LocalDateTime dateTime, String description, int calories, boolean excess, long id) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
        this.id = id;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public long getId() {
        return id;
    }

    public boolean isExcess() {
        return excess;
    }

    public String getFormattedDate() {
        return FORMATTER.format(dateTime);
    }
}
