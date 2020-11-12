package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
public class JspMealController extends AbstractMealController {

    public JspMealController(MealService service) {
        super(service);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getAll(Model model, HttpServletRequest request) {
        List<MealTo> meals;
        String filtered = request.getParameter("action");
        if (filtered != null && filtered.equals("filter")) {
            LocalDate startLocalDate = DateTimeUtil.parseLocalDate(request.getParameter("startDate"));
            LocalDate endLocalDate = DateTimeUtil.parseLocalDate(request.getParameter("endDate"));
            LocalTime startLocalTime = DateTimeUtil.parseLocalTime(request.getParameter("startTime"));
            LocalTime endLocaltime = DateTimeUtil.parseLocalTime(request.getParameter("endTime"));
            meals = MealsUtil.getFilteredTos(service.getBetweenInclusive(startLocalDate, endLocalDate, SecurityUtil.authUserId()),
                    SecurityUtil.authUserCaloriesPerDay(), startLocalTime, endLocaltime);
            log.debug("Get filtered meal collection");
        } else {
            meals = MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
            log.debug("Get unfiltered meal collection");
        }
        model.addAttribute("meals", meals);
        log.debug("Forward to meals.jsp");
        return "meals";
    }

    @RequestMapping(method = RequestMethod.POST)
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
            log.debug("Update a meal with id={}", mealId);
        } else {
            service.create(toSave, SecurityUtil.authUserId());
            log.debug("Save a new meal");
        }
        log.debug("Redirect to /meals");
        return "redirect:/meals";
    }

    @RequestMapping("/create")
    public String create(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        Integer mealId = id == null ? null : Integer.parseInt(id);
        if (mealId != null) {
            model.addAttribute("meal", service.get(mealId, SecurityUtil.authUserId()));
            log.debug("Retrieve an existing meal for mealForm.jsp page");
        } else {
            model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), "Default description", 2000));
            log.debug("Create a new meal for mealForm.jsp page");
        }
        log.debug("Forward to mealForm.jsp");
        return "mealForm";
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam("id") int mealId,
                         Model model) {
        service.delete(mealId, SecurityUtil.authUserId());
        log.debug("Delete a meal with id={}. Redirect to /meals", mealId);
        return "redirect:/meals";
    }
}
