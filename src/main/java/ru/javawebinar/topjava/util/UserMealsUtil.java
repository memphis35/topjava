package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
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

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> result = new ArrayList<>();
        Map<LocalDate, Integer> checkerMap = new HashMap<>();
        for (UserMeal currentMeal : meals) {
            LocalDate currentDate = currentMeal.getDateTime().toLocalDate();
            if (!checkerMap.containsKey(currentMeal.getDateTime().toLocalDate())) {
                checkerMap.put(currentDate, currentMeal.getCalories());
            } else {
                checkerMap.replace(currentDate, checkerMap.get(currentDate) + currentMeal.getCalories());
            }
        }
        for (UserMeal currentMeal : meals) {
            if (TimeUtil.isBetweenHalfOpen(currentMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(currentMeal.getUserMealWithExcess(checkerMap.get(currentMeal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> checkerMap = new HashMap<>();
        List<UserMealWithExcess> result = new ArrayList<>();
        meals.stream()
                .peek(meal -> {
                    LocalDate currentDate = meal.getDateTime().toLocalDate();
                    if (!checkerMap.containsKey(meal.getDateTime().toLocalDate())) {
                        checkerMap.put(currentDate, meal.getCalories());
                    } else {
                        checkerMap.replace(currentDate, checkerMap.get(currentDate) + meal.getCalories());
                    }
                })
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.toList())
                .forEach(meal -> result.add(meal.getUserMealWithExcess(checkerMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay)));
        return result;
    }
}
