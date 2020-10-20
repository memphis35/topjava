package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.Util;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-jdbc-repo.xml",
        "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal expected1 = mealsFromUser.get(mealsFromUser.size() - 1);
        Meal expected2 = mealsFromAdmin.get(mealsFromAdmin.size() - 1);
        assertMatch(service.get(meal100002.getId(), USER_ID), expected1);
        assertMatch(service.get(meal100009.getId(), ADMIN_ID), expected2);
    }

    @Test
    public void getFailedWithNotExistedMeal() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_EXISTING_MEAL_ID, USER_ID));
    }

    @Test
    public void getFailedWithAnotherMeal() {
        assertThrows(NotFoundException.class, () -> service.get(EXISTING_MEAL_ID_FROM_USER, ADMIN_ID));
    }

    @Test
    public void getFailedWithNotExistedUser() {
        assertThrows(NotFoundException.class, () -> service.get(EXISTING_MEAL_ID_FROM_USER, NOT_FOUND));
    }

    @Test
    public void delete() {
        service.delete(meal100002.getId(), USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(meal100002.getId(), USER_ID));
        service.create(new Meal(meal100002), USER_ID);
    }

    @Test
    public void deleteNotExistingMealFailed() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_EXISTING_MEAL_ID, USER_ID));
    }

    @Test
    public void deleteAnotherUserExistingMealFailed() {
        assertThrows(NotFoundException.class, () -> service.delete(EXISTING_MEAL_ID_FROM_USER, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate start = LocalDate.of(2020, 1, 31);
        List<Meal> expected = mealsFromUser.stream()
                .filter(meal -> Util.isBetweenHalfOpen(meal.getDate(), start, start.plusDays(1)))
                .collect(Collectors.toList());
        assertMatch(service.getBetweenInclusive(start, start, USER_ID), expected);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), mealsFromUser);
        assertMatch(service.getAll(ADMIN_ID), mealsFromAdmin);
    }

    @Test
    public void update() {
        Meal expected = new Meal(meal100002.getId(), LocalDateTime.of(9999, 9, 9, 9, 9), "Updated meal", 9999);
        service.update(expected, USER_ID);
        assertMatch(service.get(meal100002.getId(), USER_ID), expected);
    }

    @Test
    public void updateFailed() {
        Meal expected = new Meal(NOT_EXISTING_MEAL_ID, LocalDateTime.of(9999, 9, 9, 9, 9), "Updated meal", 9999);
        assertThrows(NotFoundException.class, () -> service.update(expected, USER_ID));
    }

    @Test
    public void create() {
        Meal expected = getNewMeal();
        Meal created = service.create(expected, USER_ID);
        expected.setId(created.getId());
        assertMatch(created, expected);
        assertMatch(service.get(expected.getId(), USER_ID), expected);
        service.delete(expected.getId(), USER_ID);
    }
}