package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.TestMatcher;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;

import java.util.Set;

import static ru.javawebinar.topjava.UserTestData.admin;
import static ru.javawebinar.topjava.MealTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {
    private final TestMatcher<User> matcher = TestMatcher.usingIgnoringFieldsComparator("registered", "roles");

    @Test
    @Transactional
    public void getWithMeals() {
        User founded = service.getWithMeals(admin.getId());
        admin.setMeals(Set.of(adminMeal1, adminMeal2));
        matcher.assertMatch(founded, admin);
    }
}
