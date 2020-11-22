package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

public class MealRestControllerTest extends AbstractControllerTest {

    @Autowired
    MealService mealService;

    private static final String URL = MealRestController.REST_URL + "/";

    @Test
    void create() throws Exception {
        Meal created = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(URL + "create")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(created)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Meal retrieved = TestUtil.readFromJson(action, Meal.class);
        created.setId(retrieved.getId());
        MEAL_MATCHER.assertMatch(retrieved, created);
        MEAL_MATCHER.assertMatch(mealService.get(retrieved.getId(), USER_ID), retrieved);
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "get/" + MealTestData.MEAL1_ID))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + "delete/" + MEAL1_ID))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertThrows(NotFoundException.class, () -> mealService.delete(MEAL1_ID, USER_ID));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MEALTO_MATCHER.contentJson(viewUserMeals));
    }

    @Test
    void update() throws Exception {
        Meal updated = getUpdated();
        perform(MockMvcRequestBuilders.put(URL + "update/" + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        MEAL_MATCHER.assertMatch(updated, mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "filter" + FILTER_PERIOD))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MEALTO_MATCHER.contentJson(viewUserMealsFiltered));
    }
}
