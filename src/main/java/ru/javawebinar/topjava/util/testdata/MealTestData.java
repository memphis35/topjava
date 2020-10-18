package ru.javawebinar.topjava.util.testdata;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.*;

public class MealTestData {
    public static final List<Meal> mealsFromUser = Arrays.asList(
            new Meal(100010, LocalDateTime.of(2020, 10, 17, 9, 15),  "User breakfast1", 350),
        new Meal(100011, LocalDateTime.of(2020, 10, 17, 13, 30),  "User lunch1", 400),
        new Meal(100012, LocalDateTime.of(2020, 10, 17, 18, 25),  "User dinner1", 300),
        new Meal(100013, LocalDateTime.of(2020, 10, 18, 7, 0),  "User breakfast2", 550),
        new Meal(100014, LocalDateTime.of(2020, 10, 18, 10, 5),  "User lunch2-1", 650),
        new Meal(100015, LocalDateTime.of(2020, 10, 18, 12, 10),  "User lunch2-2", 650),
        new Meal(100016, LocalDateTime.of(2020, 10, 18, 19, 55),  "User dinner2", 700)
    );

    public static final List<Meal> mealsFromAdmin = Arrays.asList(
            new Meal(100017, LocalDateTime.of(2020, 10, 17, 9, 15),  "Admin breakfast1", 350),
            new Meal(100018, LocalDateTime.of(2020, 10, 17, 13, 30),  "Admin lunch1", 400),
            new Meal(100019, LocalDateTime.of(2020, 10, 17, 18, 25),  "Admin dinner1", 300),
            new Meal(100020, LocalDateTime.of(2020, 10, 18, 7, 0),  "Admin breakfast2", 550),
            new Meal(100021, LocalDateTime.of(2020, 10, 18, 10, 5),  "Admin lunch2-1", 650),
            new Meal(100022, LocalDateTime.of(2020, 10, 18, 12, 10),  "Admin lunch2-2", 650),
            new Meal(100023, LocalDateTime.of(2020, 10, 18, 19, 55),  "Admin dinner2", 700)
    );

    static {
        mealsFromUser.sort(Comparator.comparing(Meal::getDateTime).reversed());
        mealsFromAdmin.sort(Comparator.comparing(Meal::getDateTime).reversed());
    }

    public static Meal getNewMeal() {
        return new Meal(null, LocalDateTime.of(2020, 1, 1, 12, 0), "New Created Meal", 2000);
    }
}
