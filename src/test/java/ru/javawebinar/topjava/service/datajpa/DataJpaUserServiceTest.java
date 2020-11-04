package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;
import static ru.javawebinar.topjava.UserTestData.admin;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    public void getWithMeals() {
        User foundUser = service.getWithMeals(admin.getId());
        admin.setMeals(List.of(adminMeal2, adminMeal1));
        USER_MATCHER.assertMatch(foundUser, admin);
        MEAL_MATCHER.assertMatch(foundUser.getMeals(), admin.getMeals());
    }

    @Test
    public void getWithoutMeals() {
        User newUser = service.create(UserTestData.getNew());
        User foundUser = service.getWithMeals(newUser.getId());
        Assert.assertTrue(foundUser.getMeals().isEmpty());
    }
}
