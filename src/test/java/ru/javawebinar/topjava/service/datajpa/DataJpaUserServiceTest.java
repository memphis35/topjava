package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.adminMeal1;
import static ru.javawebinar.topjava.MealTestData.adminMeal2;
import static ru.javawebinar.topjava.UserTestData.admin;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    @Transactional
    public void getWithMeals() {
        User foundUser = service.getWithMeals(admin.getId());
        admin.setMeals(List.of(adminMeal2, adminMeal1));
        assertThat(foundUser).usingRecursiveComparison().ignoringFields("registered", "roles", "meals").isEqualTo(admin);
        assertThat(admin.getMeals()).usingRecursiveComparison().ignoringFields("user").isEqualTo(foundUser.getMeals());
    }

    @Test
    @Transactional
    public void getWithoutMeals() {
        User newUser = service.create(UserTestData.getNew());
        User foundUser = service.getWithMeals(newUser.getId());
        Assert.assertTrue(foundUser.getMeals().isEmpty());
    }
}
