package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(MealRestController.REST_URL)
public class MealRestController extends AbstractMealController {
    public static final String REST_URL = "/rest_meals";

    @GetMapping(value = "/get/{mealId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Meal get(@PathVariable int mealId) {
        return super.get(mealId);
    }

    @DeleteMapping("/delete/{mealId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int mealId) {
        super.delete(mealId);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Meal create(@RequestBody Meal meal) {
        return super.create(meal);
    }

    @PutMapping(value = "/update/{mealId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable Integer mealId) {
        super.update(meal, mealId);
    }

    @GetMapping(value = "/filter/{startDateTime}/{endDateTime}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealTo> getBetween(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        List<MealTo> result = super.getBetween(
                startDateTime.toLocalDate(), startDateTime.toLocalTime(),
                endDateTime.toLocalDate(), endDateTime.toLocalTime());
        return result;
    }
}