package ru.javawebinar.topjava.service.user.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.user.jdbc.JdbcUserServiceTest;

@ActiveProfiles({"postgres"})
public class PgsqldbJdbcUserServiceTest extends JdbcUserServiceTest {
}
