package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealStorage {

    void save(Meal meal);

    void update(Meal meal);

    void remove(Meal meal);

    Meal get(Meal meal);

    List<Meal> getAll();
}
