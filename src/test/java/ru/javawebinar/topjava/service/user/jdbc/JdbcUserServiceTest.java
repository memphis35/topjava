package ru.javawebinar.topjava.service.user.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.user.UserServiceTest;

@ActiveProfiles("jdbc")
public abstract class JdbcUserServiceTest extends UserServiceTest {
}
