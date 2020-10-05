package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealStorage {
    Meal create(Meal meal);

    Meal update(Meal meal);

    void remove(long id);

    Meal get(long id);

    List<Meal> getAll();
}
