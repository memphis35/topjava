package ru.javawebinar.topjava.storage;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class MapMealStorage implements MealStorage {
    private static final Logger log = getLogger(MapMealStorage.class);

    private static final AtomicLong counter = new AtomicLong(0);

    private final ConcurrentMap<Long, Meal> storage = new ConcurrentHashMap<>();

    {
        Meal meal1 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        Meal meal2 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
        Meal meal3 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
        Meal meal4 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
        Meal meal5 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
        Meal meal6 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
        Meal meal7 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
        create(meal1);
        create(meal2);
        create(meal3);
        create(meal4);
        create(meal5);
        create(meal6);
        create(meal7);
    }

    @Override
    public Meal create(Meal meal) {
        long id = counter.getAndAdd(1);
        meal.setId(id);
        log.debug("create meal with id " + id);
        return storage.put(id, meal);
    }

    @Override
    public Meal update(Meal meal) {
        log.debug("update meal with id " + meal.getId());
        return storage.replace(meal.getId(), meal);
    }

    @Override
    public void remove(long id) {
        storage.remove(id);
        log.debug("remove meal with id " + id);
    }

    @Override
    public Meal get(long id) {
        Meal meal;
        if (id >= 0) {
            meal = storage.get(id);
        } else {
            meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), null, 0);
            meal.setId(-1);
        }
        log.debug("get meal with id " + meal.getId());
        return meal;
    }

    public List<Meal> getAll() {
        log.debug("Get meal list");
        return storage.values().stream().sorted(Comparator.comparing(Meal::getDateTime)).collect(Collectors.toList());
    }
}


