package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class ResourceControllerTest extends AbstractControllerTest {

    @Test
    void checkStyles() throws Exception {
        perform(MockMvcRequestBuilders.get("/resources/css/style.css"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/css;charset=UTF-8"));
    }
}
