package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository extends AbstractJdbcRepository implements UserRepository {

    private final static String GET = "SELECT * FROM users u LEFT JOIN user_roles r ON u.id=r.user_id WHERE id=?";
    private final static String GET_BY_EMAIL = "SELECT * FROM users u LEFT JOIN user_roles r ON u.id=r.user_id WHERE email=?";

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        super.validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        List<Object[]> params = new ArrayList<>();
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            user.getRoles().forEach(role -> params.add(new Object[]{user.getId(), role.name()}));
        } else {
            int userQuery = namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource);
            if (userQuery == 0) {
                return null;
            }
            user.getRoles().forEach(role -> params.add(new Object[]{user.getId(), role.name()}));
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        }
        jdbcTemplate.batchUpdate("INSERT INTO user_roles VALUES (?, ?)", params);
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    public User get(int id) {
        return get0(GET, id);
    }

    private <T> User get0(String query, T parameter) {
        List<User> users = jdbcTemplate.query(query, (rs, row) -> getUser(rs, rs.getInt("id")), parameter);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        return get0(GET_BY_EMAIL, email);
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        jdbcTemplate.query(
                "SELECT * FROM users u LEFT JOIN user_roles r ON u.id=r.user_id ORDER BY u.name, u.email",
                (RowMapper<User>) (rs, rowNum) -> {
                    System.out.println(rowNum);
                    do {
                        int userId = rs.getInt("id");
                        User user = getUser(rs, userId);
                        users.add(user);
                    } while (!rs.isAfterLast());
                    return null;
                });
        return users;
    }

    private User getUser(ResultSet rs, int userId) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setCaloriesPerDay(rs.getInt("calories_per_day"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setEnabled(rs.getBoolean("enabled"));
        user.setRegistered(rs.getDate("registered"));

        Set<Role> roles = new HashSet<>();
        boolean haveNext;
        do {
            roles.add(Role.valueOf(rs.getString("role")));
            haveNext = rs.next();
        } while (haveNext && rs.getInt("id") == userId);
        user.setRoles(roles);
        return user;
    }
}
