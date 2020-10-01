package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.controller.MealController;
import ru.javawebinar.topjava.storage.ListMealStorage;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.util.MealStorageFiller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        req.setAttribute("mealResultList", controller.getListMealTo());
        getServletContext().getRequestDispatcher("/WEB-INF/jsp/meals_summary.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
