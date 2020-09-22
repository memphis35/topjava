package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
            boolean isExceedByDate = excessCheckingMap.get(currentMeal.getDate()) > caloriesPerDay;
            if (TimeUtil.isBetweenHalfOpen(currentMeal.getTime(), startTime, endTime)) {
                result.add(UserMealConverter.toUserMealWithExcess(currentMeal, isExceedByDate));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> excessCheckingMap = meals.stream()
                .collect(HashMap::new, (map, meal) -> map.merge(meal.getDate(), meal.getCalories(), Integer::sum), (map1, map2) -> {});
        return meals.stream()
                .filter((meal) -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .collect(ArrayList::new,
                        (list, meal) -> list.add(UserMealConverter.toUserMealWithExcess(meal, excessCheckingMap.get(meal.getDate()) > caloriesPerDay)),
                        (list1, list2) -> {});
    }

    public static List<UserMealWithExcess> filteredByCyclesOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, CaloriesTotalPerDay> excessCheckingMap = new HashMap<>();
        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            excessCheckingMap.merge(meal.getDate(), new CaloriesTotalPerDay(meal, caloriesPerDay),
                    (calories1, calories2) -> {
                        calories1.addCalories(meal.getCalories(), caloriesPerDay);
                        return calories1;
                    }
            );
            AtomicBoolean isExceed = excessCheckingMap.get(meal.getDate()).getIsExceed();
            if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), isExceed));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreamsOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream().collect(new MealCollectorOptional(startTime, endTime, caloriesPerDay));
    }
}
