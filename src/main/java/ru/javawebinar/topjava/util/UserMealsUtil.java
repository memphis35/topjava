package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

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
        Map<LocalDate, MealSummaryPerDay> checkerMap = new HashMap<>();
        for (UserMeal currentMeal : meals) {
            LocalDate currentDate = currentMeal.getDateTime().toLocalDate();
            LocalTime currentTime = currentMeal.getDateTime().toLocalTime();
            if (!checkerMap.containsKey(currentDate)) {
                MealSummaryPerDay newMeal = new MealSummaryPerDay();
                checkerMap.put(currentDate, newMeal);
            }
            MealBooleanWrapper tempBoolean = checkerMap.get(currentDate).getExceed();
            if (TimeUtil.isBetweenHalfOpen(currentTime, startTime, endTime)) {
                result.add(currentMeal.getUserMealWithExcess(tempBoolean));
            }
            checkerMap.get(currentDate).addCalories(currentMeal.getCalories(), caloriesPerDay);
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> checkerMap = new HashMap<>();
        return meals.stream().collect(new Collector<UserMeal, List<UserMeal>, List<UserMealWithExcess>>() {

            @Override
            public Supplier<List<UserMeal>> supplier() {
                return ArrayList::new;
            }

            @Override
            public BiConsumer<List<UserMeal>, UserMeal> accumulator() {
                return (list, meal) -> {
                    LocalDate currentDate = meal.getDateTime().toLocalDate();
                    if (!checkerMap.containsKey(currentDate)) {
                        checkerMap.put(currentDate, meal.getCalories());
                    } else {
                        checkerMap.merge(currentDate, meal.getCalories(), Integer::sum);
                    }
                    if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                        list.add(meal);
                    }
                };
            }

            @Override
            public BinaryOperator<List<UserMeal>> combiner() {
                return (x, y) -> new ArrayList<>();
            }

            @Override
            public Function<List<UserMeal>, List<UserMealWithExcess>> finisher() {
                return userMeals -> {
                    List<UserMealWithExcess> result = new ArrayList<>();
                    for (UserMeal meal : userMeals) {
                        if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                            result.add(meal.getUserMealWithExcess(
                                    new MealBooleanWrapper(checkerMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay)));
                        }
                    }
                    return result;
                };
            }

            @Override
            public Set<Characteristics> characteristics() {
                return new HashSet<>();
            }
        });
    }

    private static class MealSummaryPerDay {
        private final MealBooleanWrapper exceed = new MealBooleanWrapper(false);
        private int totalCalories = 0;

        private void addCalories(int calories, int limit) {
            totalCalories += calories;
            if (!exceed.isExceed() && totalCalories > limit) exceed.setExceed(true);
        }

        public MealBooleanWrapper getExceed() {
            return exceed;
        }
    }

}
