package ru.javawebinar.topjava.repository.jdbc;

import javax.validation.*;
import java.util.Set;

public abstract class AbstractJdbcRepository {

    protected static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();;
    protected static final Validator validator = validatorFactory.getValidator();

    protected <T> void validate(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
    }
}
