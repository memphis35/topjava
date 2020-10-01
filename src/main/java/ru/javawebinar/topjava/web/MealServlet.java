package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.controller.MealController;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.ListMealStorage;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.util.MealStorageFiller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class MealServlet extends HttpServlet {

    private final MealStorage storage = new ListMealStorage();
    private final MealController controller = new MealController();

    @Override
    public void init() throws ServletException {
        super.init();
        MealStorageFiller.fillStorage(storage);
        controller.setModel(storage);
        controller.setCaloriesLimit(2000);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action != null) {
            switch (action) {
                case "create":
                    req.getRequestDispatcher("/WEB-INF/jsp/create.jsp").forward(req, resp);
                    break;
                case "delete":
                    storage.remove(createMeal(req));
                    break;
                case "update":
                    req.setAttribute("meal", storage.get(createMeal(req)));
                    req.getRequestDispatcher("/WEB-INF/jsp/update.jsp").forward(req, resp);
                    break;
                default:
                    throw new ServletException();
            }
        }
        req.setAttribute("mealResultList", controller.getListMealTo());
        getServletContext().getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        switch (action) {
            case "save":
                storage.save(createMeal(req));
                break;
            case "update":
                storage.update(createMeal(req));
                break;
            default:
                throw new ServletException();
        }
        resp.sendRedirect("mealApp");
    }

    private Meal createMeal(HttpServletRequest request) {
        String dateTimeValue = request.getParameter("datetime");
        String description = request.getParameter("description");
        String strCalories = request.getParameter("calories");
        int calories = strCalories != null ? Integer.parseInt(strCalories) : 0;
        LocalDateTime datetime = LocalDateTime.parse(dateTimeValue);
        return new Meal(datetime, description != null ? description : "defaultValue", calories);
    }
}
