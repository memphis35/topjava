package ru.javawebinar.topjava.service.meal.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.TestMatcher;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.meal.MealServiceTest;

import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.MealTestData.*;


import java.util.List;

@ActiveProfiles("datajpa")
public abstract class DataJpaMealServiceTest extends MealServiceTest {

    @Test
    public void getMealWithUser() {
        Meal expectedMeal = adminMeal1;
        expectedMeal.setUser(admin);
        Meal foundMeal = service.get(adminMeal1.getId(), admin.getId());
        MEAL_MATCHER.assertMatch(foundMeal, expectedMeal);
        USER_MATCHER.assertMatch(foundMeal.getUser(), admin);
    }
}
