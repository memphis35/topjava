package ru.javawebinar.topjava.service.user.springdata;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.user.UserServiceTest;

@ActiveProfiles({"postgres"})
public class PgsqldbDataJpaUserServiceTest extends DataJpaUserServiceTest {
}
