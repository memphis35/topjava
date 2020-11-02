package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealServiceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.adminMeal1;
import static ru.javawebinar.topjava.UserTestData.admin;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {

    @Test
    @Transactional
    public void getMealWithUser() {
        Meal expectedMeal = adminMeal1;
        expectedMeal.setUser(admin);
        Meal foundMeal = service.getWithUser(adminMeal1.getId(), admin.getId());
        assertThat(foundMeal).usingRecursiveComparison().ignoringFields("user").isEqualTo(expectedMeal);
        assertThat(foundMeal.getUser()).usingRecursiveComparison().ignoringFields("meals", "registered").isEqualTo(admin);
    }
}
