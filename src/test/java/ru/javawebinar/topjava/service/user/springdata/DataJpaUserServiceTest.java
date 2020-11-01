package ru.javawebinar.topjava.service.user.springdata;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.user.UserServiceTest;

@ActiveProfiles("datajpa")
public abstract class DataJpaUserServiceTest extends UserServiceTest {
}
