package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping
    public String getAll(Model model) {
        if (!model.containsAttribute("meals")) {
            model.addAttribute("meals", super.getAll());
        }
        log.debug("Forward to meals.jsp");
        return "meals";
    }

    @PostMapping
    public String save(
            @RequestParam("dateTime") String datetime,
            @RequestParam("description") String description,
            @RequestParam("calories") int calories,
            Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        Integer mealId = id.isBlank() ? null : Integer.parseInt(id);
        Meal mealToSave = new Meal(LocalDateTime.parse(datetime), description, calories);
        if (mealId != null) {
            mealToSave.setId(mealId);
            super.update(mealToSave, mealId);
        } else {
            super.create(mealToSave);
        }
        log.debug("Redirect to /meals");
        return "redirect:/meals";
    }

    @RequestMapping("/create")
    public String createMeal(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        Integer mealId = id == null ? null : Integer.parseInt(id);
        if (mealId != null) {
            model.addAttribute("meal", get(mealId));
        } else {
            model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), "Default description", 2000));
            log.debug("Create a new meal for mealForm.jsp page");
        }
        log.debug("Forward to mealForm.jsp");
        return "mealForm";
    }

    @RequestMapping("/delete")
    public String deleteMeal(@RequestParam("id") int mealId,
                             Model model) {
        delete(mealId);
        log.debug("Redirect to /meals");
        return "redirect:/meals";
    }

    @RequestMapping("/filtered")
    public String filterAll(HttpServletRequest request, Model model) {
        LocalDate startLocalDate = DateTimeUtil.parseLocalDate(request.getParameter("startDate"));
        LocalDate endLocalDate = DateTimeUtil.parseLocalDate(request.getParameter("endDate"));
        LocalTime startLocalTime = DateTimeUtil.parseLocalTime(request.getParameter("startTime"));
        LocalTime endLocalTime = DateTimeUtil.parseLocalTime(request.getParameter("endTime"));
        List<MealTo> meals = super.getBetween(startLocalDate, startLocalTime, endLocalDate, endLocalTime);
        log.debug("Get filtered meal collection");
        model.addAttribute("meals", meals);
        return getAll(model);
    }
}
