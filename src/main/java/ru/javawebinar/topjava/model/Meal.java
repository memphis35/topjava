package ru.javawebinar.topjava.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.MEAL_GET, query = "SELECT m FROM Meal m WHERE m.user.id=?1 AND m.id=:id"),
        @NamedQuery(name = Meal.MEAL_GETALL, query = "SELECT m FROM Meal m WHERE m.user.id=?1 ORDER BY m.dateTime DESC"),
        @NamedQuery(name = Meal.MEAL_GETALL_FILTERED, query = "SELECT m FROM Meal m WHERE m.user.id=?1 AND m.dateTime>=?2 AND m.dateTime<?3 ORDER BY m.dateTime DESC"),
        @NamedQuery(name = Meal.MEAL_DELETE, query = "DELETE FROM Meal m WHERE m.user.id=?1 AND m.id=?2")
})
@Entity
@Table(name = "meals", uniqueConstraints = @UniqueConstraint(name = "meals_unique_datetime_idx", columnNames = {"user_id", "date_time"}))
public class Meal extends AbstractBaseEntity {
    public static final String MEAL_GET = "Meal.get";
    public static final String MEAL_GETALL = "Meal.getAll";
    public static final String MEAL_GETALL_FILTERED = "Meal.getAllFiltered";
    public static final String MEAL_DELETE = "Meal.delete";

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false)
    @NotNull
    private String description;

    @Column(name = "calories", nullable = false)
    private int calories;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
