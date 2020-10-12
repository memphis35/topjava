package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final MealService service;

    private final Map<String, String> filterMap = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        ValidationUtil.checkNew(meal);
        meal = service.create(meal, authUserId());
        log.debug("User #{} has created meal with id={} successfully", authUserId(), meal.getId());
        return meal;
    }

    public Meal update(Meal meal) {
        ValidationUtil.assureIdConsistent(meal, meal.getId());
        meal = service.create(meal, authUserId());
        log.debug("User #{} has updated meal with id={} successfully", authUserId(), meal.getId());
        return meal;
    }

    public boolean delete(Integer mealId) {
        boolean deleted = service.delete(mealId, authUserId());
        log.debug("User #{} has removed meal with id={} successfully", authUserId(), mealId);
        return deleted;
    }

    public Meal get(Integer id) {
        Meal result = service.get(id, authUserId());
        log.debug("User #{} has received meal with id={} successfully", authUserId(), id);
        return result;
    }

    public Collection<Meal> getAll() {
        log.debug("User #{} is getting list of meals", authUserId());
        return service.getAll(authUserId());
    }

    public Meal generate() {
        log.debug("Generating a new meal");
        return new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
    }

    public List<MealTo> getMealToList() {
        log.debug("Converting List of meals to List of MealTOs");
        if (filterMap.isEmpty()) {
            return MealsUtil.getFilteredTos(getAll(), authUserCaloriesPerDay(), LocalDate.MIN, LocalDate.MAX, LocalTime.MIN, LocalTime.MAX);
        } else {
            LocalDate startDate = filterMap.get("startDate").isEmpty() ? LocalDate.MIN : DateTimeUtil.convertToDate(filterMap.get("startDate"));
            LocalDate endDate = filterMap.get("endDate").isEmpty() ? LocalDate.MAX : DateTimeUtil.convertToDate(filterMap.get("endDate"));
            LocalTime startTime = filterMap.get("startTime").isEmpty() ? LocalTime.MIN : DateTimeUtil.convertToTime(filterMap.get("startTime"));
            LocalTime endTime = filterMap.get("endTime").isEmpty() ? LocalTime.MAX : DateTimeUtil.convertToTime(filterMap.get("endTime"));
            filterMap.clear();
            return MealsUtil.getFilteredTos(getAll(), authUserCaloriesPerDay(), startDate, endDate, startTime, endTime);
        }
    }

    public void setFilterMap(Map<String, String> map) {
        this.filterMap.putAll(map);
    }
}