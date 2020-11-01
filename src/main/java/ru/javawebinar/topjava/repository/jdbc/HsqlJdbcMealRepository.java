package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;

@Repository
@Profile("hsqldb")
public class HsqlJdbcMealRepository extends JdbcMealRepository { //2020-01-30 10:00:00
    public HsqlJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        datetimeConverter = localDateTime -> localDateTime.format(formatter);
    }
}
