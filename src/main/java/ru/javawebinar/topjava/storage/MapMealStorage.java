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
        return meal;
    }

    @Override
    public Meal update(Meal meal) {
        if (storage.containsKey(meal.getId())) {
            storage.replace(meal.getId(), meal);
        } else {
            return null;
        }
        log.debug("update meal with id {}", meal.getId());
        return meal;
    }

    @Override
    public void remove(long id) {
        storage.remove(id);
        log.debug("remove meal with id {}", id);
    }

    @Override
    public Meal get(long id) {
        log.debug("get meal with id {}", id);
        return storage.get(id);
    }

    public List<Meal> getAll() {
        log.debug("get meal list");
        return new ArrayList<>(storage.values());
    }
}


