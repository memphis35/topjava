package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private ClassPathXmlApplicationContext context;

    private MealRestController mealController;

    @Override
    public void init() {
        context = new ClassPathXmlApplicationContext(getServletContext().getInitParameter("springXmlFile"));
        mealController = context.getBean("mealRestController", MealRestController.class);
        AdminRestController userController = context.getBean("adminRestController", AdminRestController.class);
        userController.create(new User(null, "User1", "user1@email.com", "securitySucks", Role.USER));
        userController.create(new User(null, "User2", "user2@email.com", "securitySucks", Role.USER));
    }

    @Override
    public void destroy() {
        context.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (request.getParameter("userId") != null) {
            SecurityUtil.setUserId(Integer.parseInt(request.getParameter("userId")));
        } else {
            Meal meal = generateMeal(request);
            if (meal.isNew()) {
                log.info("Create {}", meal);
                mealController.create(meal);
            } else {
                log.info("Update {}", meal);
                mealController.update(meal);
            }
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ? MealsUtil.generate() : mealController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                if (request.getParameter("filter") != null) {
                    request.setAttribute("meals", mealController.getMealToList(
                            request.getParameter("startDate"),
                            request.getParameter("endDate"),
                            request.getParameter("startTime"),
                            request.getParameter("endTime")));
                } else {
                    request.setAttribute("meals", mealController.getMealToList("", "", "", ""));
                }
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private Meal generateMeal(HttpServletRequest request) {
        String id = request.getParameter("id");
        return new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
    }
}
