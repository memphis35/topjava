package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int EXISTING_MEAL_ID_FROM_USER = 100002;

    public static final int NOT_EXISTING_MEAL_ID = 123456;

    public static final Meal meal100002 = new Meal(100002, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "User breakfast1", 500);
    public static final Meal meal100003 = new Meal(100003, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "User lunch1", 1000);
    public static final Meal meal100004 = new Meal(100004, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "User dinner1", 500);
    public static final Meal meal100005 = new Meal(100005, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "User breakfast2", 100);
    public static final Meal meal100006 = new Meal(100006, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "User lunch2-1", 1000);
    public static final Meal meal100007 = new Meal(100007, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "User lunch2-2", 500);
    public static final Meal meal100008 = new Meal(100008, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "User dinner2", 410);

    public static final Meal meal100009 = new Meal(100009, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Admin breakfast1", 500);
    public static final Meal meal100010 = new Meal(100010, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Admin lunch1", 1000);
    public static final Meal meal100011 = new Meal(100011, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Admin dinner1", 500);
    public static final Meal meal100012 = new Meal(100012, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Admin breakfast2", 100);
    public static final Meal meal100013 = new Meal(100013, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Admin lunch2-1", 1000);
    public static final Meal meal100014 = new Meal(100014, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Admin lunch2-2", 500);
    public static final Meal meal100015 = new Meal(100015, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Admin dinner2", 410);

    public static List<Meal> mealsFromUser = new ArrayList<>();
    public static List<Meal> mealsFromAdmin = new ArrayList<>();

    static {
        Collections.addAll(mealsFromUser, meal100002, meal100003, meal100004, meal100005, meal100006, meal100007, meal100008);
        Collections.addAll(mealsFromAdmin, meal100009, meal100010, meal100011, meal100012, meal100013, meal100014, meal100015);
        mealsFromUser.sort(Comparator.comparing(Meal::getDateTime).reversed());
        mealsFromAdmin.sort(Comparator.comparing(Meal::getDateTime).reversed());
    }

    public static Meal getNewMeal() {
        return new Meal(null, LocalDateTime.of(2020, 1, 1, 12, 0), "New Created Meal", 2000);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
