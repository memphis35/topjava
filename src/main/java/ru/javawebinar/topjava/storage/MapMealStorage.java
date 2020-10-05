package ru.javawebinar.topjava.storage;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.slf4j.LoggerFactory.getLogger;

public class MapMealStorage implements MealStorage {
    private static final Logger log = getLogger(MapMealStorage.class);

    private final AtomicLong counter = new AtomicLong(0);

    private final Map<Long, Meal> storage = new ConcurrentHashMap<>();

    @Override
    public Meal create(Meal meal) {
        long id = counter.incrementAndGet();
        meal.setId(id);
        storage.put(id, meal);
        log.debug("create meal with id {}", id);
        return storage.get(id);
    }

    @Override
    public Meal update(Meal meal) {
        storage.replace(meal.getId(), meal);
        log.debug("update meal with id {}", meal.getId());
        return storage.get(meal.getId());
    }

    @Override
    public void remove(long id) {
        storage.remove(id);
        log.debug("remove meal with id {}", id);
    }

    @Override
    public Meal get(long id) {
        log.debug("get meal with id {}", id);
        return storage.getOrDefault(id, null);
    }

    public List<Meal> getAll() {
        log.debug("get meal list");
        return new ArrayList<>(storage.values());
    }
}


