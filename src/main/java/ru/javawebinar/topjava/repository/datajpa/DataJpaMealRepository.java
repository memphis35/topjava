package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;
    private final CrudUserRepository userRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository userRepository) {
        this.crudRepository = crudRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        User user = userRepository.getOne(userId);
        if (meal.isNew()) {
            meal.setUser(user);
            return crudRepository.save(meal);
        }
        if (get(meal.getId(), userId) != null) {
            crudRepository.update(meal.getDateTime(), meal.getDescription(), meal.getCalories(), meal.getId());
            return meal;
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.deleteByIdAndUserId(id, userId) > 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.getItem(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAllMealsByUserId(userId, Sort.by(Sort.Direction.DESC, "dateTime"));
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getUserMealsFilteredByDateTime(startDateTime, endDateTime, userId);
    }
}
