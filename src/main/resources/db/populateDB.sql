DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals
VALUES
    (100010, 100000, '2020-10-17 09:15:00', 'User breakfast1', 350),
    (100011, 100000, '2020-10-17 13:30:00', 'User lunch1', 400),
    (100012, 100000, '2020-10-17 18:25:00', 'User dinner1', 300),
    (100013, 100000, '2020-10-18 07:00:00', 'User breakfast2', 550),
    (100014, 100000, '2020-10-18 10:05:00', 'User lunch2-1', 650),
    (100015, 100000, '2020-10-18 12:10:00', 'User lunch2-2', 650),
    (100016, 100000, '2020-10-18 19:55:00', 'User dinner2', 700),
    (100017, 100001, '2020-10-17 09:15:00', 'Admin breakfast1', 350),
    (100018, 100001, '2020-10-17 13:30:00', 'Admin lunch1', 400),
    (100019, 100001, '2020-10-17 18:25:00', 'Admin dinner1', 300),
    (100020, 100001, '2020-10-18 07:00:00', 'Admin breakfast2', 550),
    (100021, 100001, '2020-10-18 10:05:00', 'Admin lunch2-1', 650),
    (100022, 100001, '2020-10-18 12:10:00', 'Admin lunch2-2', 650),
    (100023, 100001, '2020-10-18 19:53:00', 'Admin dinner2', 700);