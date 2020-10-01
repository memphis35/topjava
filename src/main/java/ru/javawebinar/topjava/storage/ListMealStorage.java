package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class ListMealStorage implements MealStorage {

    private final List<Meal> storage = new LinkedList<>();

    @Override
    public void save(Meal meal) {
        doAction(meal, index -> index < 0, index -> storage.add(meal));
    }

    @Override
    public void update(Meal meal) {
        doAction(meal, index -> index > -1, index -> storage.set(index, meal));
    }

    @Override
    public void remove(Meal meal) {
        doAction(meal, index -> index > -1, storage::remove);
    }

    @Override
    public Meal get(Meal meal) {
        return doAction(meal, index -> index > -1, storage::get);
    }

    public List<Meal> getAll() {
        return storage;
    }

    private <TYPE> TYPE doAction(Meal meal, Predicate<Integer> predicate, Action<TYPE> action) {
        int index = storage.indexOf(meal);
        if (predicate.test(index)) {
            return action.action(index);
        } else {
            throw new IllegalArgumentException();
        }
    }

    interface Action<TYPE> {
        TYPE action(int index);
    }

}


