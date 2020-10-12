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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final MealService service;

    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        checkNew(meal);
        meal = service.create(meal, authUserId());
        log.debug("User #{} has created meal with id={} successfully", authUserId(), meal.getId());
        return meal;
    }

    public Meal update(Meal meal) {
        meal = service.update(meal, authUserId());
        log.debug("User #{} has updated meal with id={} successfully", authUserId(), meal.getId());
        return meal;
    }

    public void delete(Integer mealId) {
        service.delete(mealId, authUserId());
        log.debug("User #{} has removed meal with id={} successfully", authUserId(), mealId);
    }

    public Meal get(Integer id) {
        Meal result = service.get(id, authUserId());
        log.debug("User #{} has received meal with id={} successfully", authUserId(), id);
        return result;
    }

    public List<Meal> getAll() {
        log.debug("User #{} is getting list of meals", authUserId());
        return service.getAll(authUserId());
    }

    public List<MealTo> getMealToList(String strStartDate, String strEndDate, String strStartTime, String strEndTime) {
        log.debug("Converting List of meals to List of MealTOs");
        LocalDate startDate = strStartDate.isEmpty() ? LocalDate.MIN : DateTimeUtil.convertToDate(strStartDate);
        LocalDate endDate = strEndDate.isEmpty() ? LocalDate.MAX : DateTimeUtil.convertToDate(strEndDate).plusDays(1);
        LocalTime startTime = strStartTime.isEmpty() ? LocalTime.MIN : DateTimeUtil.convertToTime(strStartTime);
        LocalTime endTime = strEndTime.isEmpty() ? LocalTime.MAX : DateTimeUtil.convertToTime(strEndTime);
        return MealsUtil.getFilteredTos(getAll(), authUserCaloriesPerDay(), startDate, endDate, startTime, endTime);
    }
}