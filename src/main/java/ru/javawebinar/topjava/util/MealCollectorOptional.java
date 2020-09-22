package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class MealCollectorOptional implements Collector<UserMeal, List<UserMealWithExcess>, List<UserMealWithExcess>> {

    private final Map<LocalDate, CaloriesTotalPerDay> excessCheckingMap = new HashMap<>();
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int caloriesLimit;

    public MealCollectorOptional (LocalTime start, LocalTime end, int limit) {
        startTime = start;
        endTime = end;
        caloriesLimit = limit;
    }
    @Override
    public Supplier<List<UserMealWithExcess>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<UserMealWithExcess>, UserMeal> accumulator() {
        return (list, meal) -> {
            excessCheckingMap.merge(
                    meal.getDate(),
                    new CaloriesTotalPerDay(meal, caloriesLimit),
                    (calories1, calories2) -> {
                        calories1.addCalories(meal.getCalories(), caloriesLimit);
                        return calories1;
                    }
            );
            AtomicBoolean isExceed = excessCheckingMap.get(meal.getDate()).getIsExceed();
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                list.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), isExceed));
            }
        };
    }

    @Override
    public BinaryOperator<List<UserMealWithExcess>> combiner() {
        return (x, y) -> x;
    }

    @Override
    public Function<List<UserMealWithExcess>, List<UserMealWithExcess>> finisher() {
        return (resultList) -> resultList;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return new HashSet<>();
    }


}
