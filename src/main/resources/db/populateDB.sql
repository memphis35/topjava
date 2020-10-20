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
    (DEFAULT, 100000, '2020-01-30 10:00:00', 'User breakfast1', 500),
    (DEFAULT, 100000, '2020-01-30 13:00:00', 'User lunch1', 1000),
    (DEFAULT, 100000, '2020-01-30 20:00:00', 'User dinner1', 500),
    (DEFAULT, 100000, '2020-01-31 00:00:00', 'User breakfast2', 100),
    (DEFAULT, 100000, '2020-01-31 10:00:00', 'User lunch2-1', 1000),
    (DEFAULT, 100000, '2020-01-31 13:00:00', 'User lunch2-2', 500),
    (DEFAULT, 100000, '2020-01-31 20:00:00', 'User dinner2', 410),
    (DEFAULT, 100001, '2020-01-30 10:00:00', 'Admin breakfast1', 500),
    (DEFAULT, 100001, '2020-01-30 13:00:00', 'Admin lunch1', 1000),
    (DEFAULT, 100001, '2020-01-30 20:00:00', 'Admin dinner1', 500),
    (DEFAULT, 100001, '2020-01-31 00:00:00', 'Admin breakfast2', 100),
    (DEFAULT, 100001, '2020-01-31 10:00:00', 'Admin lunch2-1', 1000),
    (DEFAULT, 100001, '2020-01-31 13:00:00', 'Admin lunch2-2', 500),
    (DEFAULT, 100001, '2020-01-31 20:00:00', 'Admin dinner2', 410);