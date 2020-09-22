package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserMealWithExcess {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final AtomicBoolean excess;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, boolean isExceed) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        excess = new AtomicBoolean(isExceed);
    }

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, AtomicBoolean isExceed) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        excess = isExceed;
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
