package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCyclesOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreamsOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> excessCheckingMap = new HashMap<>();
        for (UserMeal currentMeal : meals) {
            excessCheckingMap.merge(currentMeal.getDate(), currentMeal.getCalories(), Integer::sum);
        }
        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal currentMeal : meals) {
            if (TimeUtil.isBetweenHalfOpen(currentMeal.getTime(), startTime, endTime)) {
                boolean isExceed = excessCheckingMap.get(currentMeal.getDate()) > caloriesPerDay;
                result.add(toUserMealWithExcess(currentMeal, isExceed));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> excessCheckingMap = meals.stream()
                        .collect(Collectors.toMap(UserMeal::getDate, UserMeal::getCalories, Integer::sum));
        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> toUserMealWithExcess(meal, excessCheckingMap.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByCyclesOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesCounterMap = new HashMap<>();
        Map<LocalDate, AtomicBoolean> excessCheckingMap = new HashMap<>();
        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            caloriesCounterMap.merge(meal.getDate(), meal.getCalories(), Integer::sum);
            AtomicBoolean isExceed = excessCheckingMap.computeIfAbsent(meal.getDate(), date -> new AtomicBoolean());
            isExceed.set(caloriesCounterMap.get(meal.getDate()) > caloriesPerDay);
            if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), isExceed));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreamsOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.parallelStream().collect(new MealCollectorOptional(startTime, endTime, caloriesPerDay));
    }

    public static UserMealWithExcess toUserMealWithExcess(UserMeal meal, boolean excess) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
