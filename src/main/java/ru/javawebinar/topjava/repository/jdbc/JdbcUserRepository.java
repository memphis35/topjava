package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
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
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository extends AbstractJdbcRepository implements UserRepository {

    private final static String GET = "SELECT * FROM users WHERE id=?";
    private final static String GET_BY_EMAIL = "SELECT * FROM users WHERE email=?";
    private final static String GET_ROLES = "SELECT * FROM user_roles WHERE user_id=?";

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
        validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        List<Object[]> params = new ArrayList<>();
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                UPDATE users SET name=:name, email=:email, password=:password, registered=:registered, enabled=:enabled, 
                calories_per_day=:caloriesPerDay WHERE id=:id""", parameterSource) == 0) {
            return null;
        } else {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        }
        user.getRoles().forEach(role -> params.add(new Object[]{user.getId(), role.name()}));
        jdbcTemplate.batchUpdate("INSERT INTO user_roles VALUES (?, ?)", params);
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    public User get(int id) {
        return getByParameter(GET, id);
    }

    private <T> User getByParameter(String query, T parameter) {
        List<User> users = jdbcTemplate.query(query, (rs, row) -> getUser(rs), parameter);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            List<Role> userRoles = jdbcTemplate.query(GET_ROLES, (rs, rowNum) ->
                    Role.valueOf(rs.getString("role")), user.getId());
            user.setRoles(userRoles);
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
        return getByParameter(GET_BY_EMAIL, email);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles r ON u.id=r.user_id ORDER BY u.name, u.email",
                (ResultSetExtractor<List<User>>) rs -> {
                    Map<Integer, User> users = new LinkedHashMap<>();
                    while (rs.next()) {
                        User user = getUser(rs);
                        users.putIfAbsent(user.getId(), user);
                        user = users.get(user.getId());
                        Role userRole = Role.valueOf(rs.getString("role"));
                        user.getRoles().add(userRole);
                    }
                    return new ArrayList<>(users.values());
                });
    }

    private User getUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setCaloriesPerDay(rs.getInt("calories_per_day"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setEnabled(rs.getBoolean("enabled"));
        user.setRegistered(rs.getDate("registered"));
        user.setRoles(new HashSet<>());
        return user;
    }
}
