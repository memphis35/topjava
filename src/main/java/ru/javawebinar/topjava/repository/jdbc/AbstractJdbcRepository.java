package ru.javawebinar.topjava.repository.jdbc;

import javax.validation.*;
import java.util.Set;

public abstract class AbstractJdbcRepository {

    protected final ValidatorFactory validatorFactory;
    protected final Validator validator;

    public AbstractJdbcRepository() {
         validatorFactory = Validation.buildDefaultValidatorFactory();
         validator = validatorFactory.getValidator();
    }

    protected <T> void validate(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
    }
}
