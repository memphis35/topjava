package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/meals")
public class JspMealController {
    private MealService service;

    @Autowired
    public JspMealController(MealService service) {
        this.service = service;
    }

    @RequestMapping("/create")
    public String create(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        Integer mealId = id == null ? null : Integer.parseInt(id);
        if (mealId != null) {
            model.addAttribute("meal", service.get(mealId, SecurityUtil.authUserId()));
        } else {
            model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), "Default description", 2000));
        }
        return "mealForm";
    }

    @RequestMapping("/save")
    public String save(
            @RequestParam("dateTime") String datetime,
            @RequestParam("description") String description,
            @RequestParam("calories") int calories,
            Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        Integer mealId = id.isBlank() ? null : Integer.parseInt(id);
        Meal toSave = new Meal(LocalDateTime.parse(datetime), description, calories);
        if (mealId != null) {
            toSave.setId(mealId);
            service.update(toSave, SecurityUtil.authUserId());
        } else {
            service.create(toSave, SecurityUtil.authUserId());
        }
        return getAll(model);
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam("id") int mealId,
                         Model model) {
        service.delete(mealId, SecurityUtil.authUserId());
        return getAll(model);
    }

    @RequestMapping("/getFiltered")
    public String getAllFiltered(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            Model model) {
        LocalDate startLocalDate = DateTimeUtil.parseLocalDate(startDate);
        LocalDate endLocalDate = DateTimeUtil.parseLocalDate(endDate);
        LocalTime startLocalTime = DateTimeUtil.parseLocalTime(startTime);
        LocalTime endLocaltime = DateTimeUtil.parseLocalTime(endTime);
        List<MealTo> filtered = MealsUtil.getFilteredTos(service.getBetweenInclusive(startLocalDate, endLocalDate, SecurityUtil.authUserId()),
                SecurityUtil.authUserCaloriesPerDay(), startLocalTime, endLocaltime);
        model.addAttribute("meals", filtered);
        return "meals";
    }

    @RequestMapping("/getAll")
    public String getAll(Model model) {
        model.addAttribute("meals",
                MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }
}
