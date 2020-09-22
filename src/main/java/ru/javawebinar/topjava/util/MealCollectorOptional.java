package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class MealCollectorOptional implements Collector<UserMeal, List<UserMealWithExcess>, List<UserMealWithExcess>> {

    private final Map<LocalDate, Integer> caloriesCounterMap = new ConcurrentHashMap<>();

    private final Map<LocalDate, AtomicBoolean> excessCheckingMap = new ConcurrentHashMap<>();

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
            caloriesCounterMap.merge(meal.getDate(), meal.getCalories(), Integer::sum);
            AtomicBoolean isExceed = new AtomicBoolean(caloriesCounterMap.get(meal.getDate()) > caloriesLimit);
            excessCheckingMap.computeIfAbsent(meal.getDate(), date -> isExceed).set(isExceed.get());
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                list.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excessCheckingMap.get(meal.getDate())));
            }
        };
    }

    @Override
    public BinaryOperator<List<UserMealWithExcess>> combiner() {
        return (list, list2) -> {
            list.addAll(list2);
            return list;
        };
    }

    @Override
    public Function<List<UserMealWithExcess>, List<UserMealWithExcess>> finisher() {
        return list -> list;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return new HashSet<>();
    }


}
