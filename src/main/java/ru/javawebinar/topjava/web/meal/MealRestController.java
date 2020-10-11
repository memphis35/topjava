package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.MealServlet;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;

public class MealRestController {
    private MealService service;

    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        if (service.create(meal, authUserId()) != null) {
            log.debug("User #{} have created meal with id={} successfully", authUserId(), meal.getId());
            return meal;
        } else {
            log.error("User #{} can't update meal with id={}", authUserId(), meal.getId());
            throw new NotFoundException("");
        }
    }

    public Meal update(Meal meal) {
        if (service.update(meal, authUserId()) != null) {
            log.debug("User #{} have updated meal with id={} successfully", authUserId(), meal.getId());
            return meal;
        } else {
            log.error("User #{} can't update meal with id={}", authUserId(), meal.getId());
            throw new NotFoundException("");
        }
    }

    public boolean delete(Integer mealId) {
        if (service.delete(mealId, authUserId())) {
            log.debug("User #{} have deleted meal with id={} successfully", authUserId(), mealId);
            return true;
        } else {
            log.error("User #{} can't delete meal with id={}", authUserId(), mealId);
            throw new NotFoundException("");
        }
    }

    public Meal get(Integer id) {
        return service.get(id, authUserId());
    }

    public Collection<Meal> getAll() {
        return service.getAll(authUserId());
    }

    public Meal generate() {
        log.debug("Generating a new meal");
        return new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
    }

    public List<MealTo> getMealToList() {
        return MealsUtil.getTos(getAll(), authUserCaloriesPerDay());
    }
}