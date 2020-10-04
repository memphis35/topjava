package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MapMealStorage;
import ru.javawebinar.topjava.storage.MealStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private final MealStorage storage = new MapMealStorage();

    private final int caloriesLimit = 2000;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action") != null ? req.getParameter("action") : "list";
        long index = req.getParameter("id") != null ? Long.parseLong(req.getParameter("id")) : -1;
        switch (action) {
            case "create":
            case "update":
                req.setAttribute("meal", storage.get(index));
                log.debug("forward to update.jsp");
                req.getRequestDispatcher("/jsp/update.jsp").forward(req, resp);
                break;
            case "delete":
                storage.remove(index);
                break;
            default:
                log.debug("Illegal action argument");
                break;
        }
        req.setAttribute("mealResultList", getListMealTo());
        log.debug("forward to meals.jsp");
        getServletContext().getRequestDispatcher("/jsp/meals.jsp").forward(req, resp);
    }

    private List<MealTo> getListMealTo() {
        Map<LocalDate, Integer> caloriesSummaryPerDay =
                storage.getAll().stream()
                        .collect(Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum));
        log.debug("Getting mealTO collection");
        return storage.getAll().stream()
                .map(meal -> new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        caloriesSummaryPerDay.get(meal.getDate()) > caloriesLimit, meal.getId()))
                .collect(Collectors.toList());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        long id = Long.parseLong(req.getParameter("id"));
        Meal current = createMeal(req);
        if ("update".equals(action)) {
            if (id >= 0) {
                current.setId(id);
                storage.update(current);
            } else {
                storage.create(current);
            }
        }
        log.debug("Redirect to MealServlet.class");
        resp.sendRedirect("meals");
    }

    private Meal createMeal(HttpServletRequest request) {
        LocalDateTime datetime = LocalDateTime.parse(request.getParameter("datetime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(datetime, description, calories);
        log.debug("Meal successfully created");
        return meal;
    }
}
