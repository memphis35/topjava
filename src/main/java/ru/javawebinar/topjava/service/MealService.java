package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.Collection;
import java.util.stream.Collectors;

public class MealService {
    private UserService userService;

    private MealRepository repository;

    public MealService(MealRepository repository, UserService service) {
        this.repository = repository;
        this.userService = service;
    }

    public Meal create(Meal meal, int authUserId) {
        userService.get(authUserId).addMealId(repository.save(meal).getId());
        return meal;
    }

    public Meal get(Integer id, int authUserId) {
        return userService.get(authUserId).isExistMealId(id) ? repository.get(id) : null;
    }

    public boolean delete(Integer id, int authUserId) {
        if (userService.get(authUserId).removeMealId(id)) {
            return repository.delete(id);
        }
        return false;
    }

    public Collection<Meal> getAll(int authUserId) {
        return repository.getAll().stream()
                .filter(meal -> userService.get(authUserId).getMeals().contains(meal.getId()))
                .collect(Collectors.toList());
    }

    public Meal update(Meal meal, int authUserId) {
        if (userService.get(authUserId).isExistMealId(meal.getId())) {
            return repository.save(meal);
        } else {
            return null;
        }
    }
}