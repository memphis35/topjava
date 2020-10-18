package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.util.testdata.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@ContextConfiguration({"classpath:spring/spring-jdbc-app.xml", "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal expected1 = MealTestData.mealsFromUser.get(0);
        Meal expected2 = MealTestData.mealsFromAdmin.get(0);
        assertEquals(expected1, service.get(100016, 100000));
        assertEquals(expected2, service.get(100023, 100001));
    }

    @Test
    public void getFailed() {
        assertThrows(NotFoundException.class, () -> service.get(123456, 100000));
        assertThrows(NotFoundException.class, () -> service.get(100002, 100001));
    }

    @Test
    public void delete() {
        service.delete(100010, 100000);
        assertThrows(NotFoundException.class, () -> service.get(100002, 100000));
    }

    @Test
    public void deleteFailed() {
        assertThrows(NotFoundException.class, () -> service.delete(123456, 100000));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate start = LocalDate.of(2020, 10, 18);
        List<Meal> expected = MealTestData.mealsFromUser.subList(0, 4);
        assertEquals(expected, service.getBetweenInclusive(start, start, 100000));
    }

    @Test
    public void getAll() {
        assertEquals(MealTestData.mealsFromUser, service.getAll(100000));
        assertEquals(MealTestData.mealsFromAdmin, service.getAll(100001));
    }

    @Test
    public void update() {
        Meal expected = new Meal(100010, LocalDateTime.of(9999, 9, 9, 9, 9), "Updated meal", 9999);
        service.update(expected, 100000);
        assertEquals(expected, service.get(100010, 100000));
    }

    @Test
    public void updateFailed() {
        Meal expected = new Meal(100020, LocalDateTime.of(9999, 9, 9, 9, 9), "Updated meal", 9999);
        assertThrows(NotFoundException.class, () -> service.update(expected, 100000));
    }

    @Test
    public void create() {
        Meal expected = MealTestData.getNewMeal();
        Meal created = service.create(expected, 100000);
        expected.setId(created.getId());
        assertEquals(expected, created);
        assertEquals(expected, service.get(expected.getId(), 100000));
    }
}