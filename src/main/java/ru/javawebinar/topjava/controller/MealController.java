package ru.javawebinar.topjava.controller;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.ListMealStorage;
import ru.javawebinar.topjava.storage.MealStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MealController {

    private MealStorage model = new ListMealStorage();
    private int caloriesLimit = 0;

    public void setModel(MealStorage model) {
        this.model = model;
    }

    public void setCaloriesLimit(int caloriesLimit) {
        this.caloriesLimit = caloriesLimit;
    }

    public List<MealTo> getListMealTo() {
        Map<LocalDate, Integer> totalCaloriesByDay =
                model.getAll().stream().collect(Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum));
        return model.getAll().stream()
                .map(meal -> createTo(meal, totalCaloriesByDay.get(meal.getDate()) > caloriesLimit))
                .collect(Collectors.toList());
    }

    public static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
