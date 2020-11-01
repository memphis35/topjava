package ru.javawebinar.topjava.service.user.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.user.jdbc.JdbcUserServiceTest;

@ActiveProfiles("hsqldb")
public class HsqldbJdbcUserServiceTest extends JdbcUserServiceTest {
}
