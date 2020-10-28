package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {
    @PersistenceContext
    private EntityManager manager;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User user = manager.getReference(User.class, userId);
        if (meal.isNew()) {
            meal.setUser(user);
            manager.persist(meal);
            return meal;
        } else {
            Meal foundedMeal = manager.find(Meal.class, meal.getId());
            return foundedMeal != null && foundedMeal.getUser().getId() == userId ? manager.merge(meal) : null;
        }

    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return manager.createNamedQuery(Meal.MEAL_DELETE)
                .setParameter(1, userId)
                .setParameter(2, id)
                .executeUpdate() == 1;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = manager.find(Meal.class, id);
        return meal != null && meal.getUser().getId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return manager.createNamedQuery(Meal.MEAL_GETALL, Meal.class)
                .setParameter(1, userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Meal> criteriaQuery = cb.createQuery(Meal.class);
        Root<Meal> mealRoot = criteriaQuery.from(Meal.class);
        criteriaQuery.select(mealRoot)
                .where(cb.greaterThanOrEqualTo(mealRoot.get("dateTime"), startDateTime),
                        cb.and(cb.lessThan(mealRoot.get("dateTime"), endDateTime)),
                        cb.and(cb.equal(mealRoot.get("user").get("id"), userId)))
                .orderBy(cb.desc(mealRoot.get("dateTime")));
        return manager.createQuery(criteriaQuery).getResultList();
        /*return manager.createNamedQuery(Meal.MEAL_GETALL_FILTERED, Meal.class)
                .setParameter(1, userId)
                .setParameter(2, startDateTime)
                .setParameter(3, endDateTime)
                .getResultList();*/
    }
}