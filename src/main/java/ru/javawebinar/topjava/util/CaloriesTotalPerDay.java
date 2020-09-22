package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;

import java.util.concurrent.atomic.AtomicBoolean;

public class CaloriesTotalPerDay {
    private final AtomicBoolean isExceed = new AtomicBoolean(false);
    private int totalCalories = 0;

    public CaloriesTotalPerDay(UserMeal meal, int limit) {
        addCalories(meal.getCalories(), limit);
    }

    public void addCalories(int calories, int limit) {
        totalCalories += calories;
        if (!isExceed.get() && totalCalories > limit) isExceed.set(true);
    }

    public AtomicBoolean getIsExceed() {
        return isExceed;
    }

    public boolean isExceed() {
        return isExceed.get();
    }
}
