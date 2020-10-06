package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MapMealStorage;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealStorage storage;

    private final int caloriesLimit = 2000;

    @Override
    public void init() throws ServletException {
        storage = new MapMealStorage();
        storage.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        storage.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        storage.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        storage.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        storage.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        storage.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        storage.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action") != null ? req.getParameter("action") : "default";
        long id = req.getParameter("id") != null ? Long.parseLong(req.getParameter("id")) : 0;
        switch (action) {
            case "create":
            case "update":
                req.setAttribute("meal", action.equals("create") ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0) : storage.get(id));
                log.debug("switch with action={}, forward to update.jsp", action);
                req.getRequestDispatcher("/jsp/update.jsp").forward(req, resp);
                break;
            case "delete":
                log.debug("switch with action={}", action);
                storage.remove(id);
                resp.sendRedirect("meals");
                break;
            default:
                req.setAttribute("mealResultList", MealsUtil.filteredByStreams(storage.getAll(), LocalTime.MIN, LocalTime.MAX, caloriesLimit));
                log.debug("forward to meals.jsp");
                getServletContext().getRequestDispatcher("/jsp/meals.jsp").forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        long id = Long.parseLong(req.getParameter("id"));
        Meal current = createMeal(req);
        log.debug("meal successfully created");
        if (id > 0) {
            current.setId(id);
            storage.update(current);
        } else {
            storage.create(current);
        }
        log.debug("Redirect to MealServlet.class");
        resp.sendRedirect("meals");
    }

    private Meal createMeal(HttpServletRequest request) {
        LocalDateTime datetime = LocalDateTime.parse(request.getParameter("datetime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        return new Meal(datetime, description, calories);
    }
}
