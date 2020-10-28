package ru.javawebinar.topjava.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.MEAL_GET, query = "SELECT m FROM Meal m WHERE m.user.id=?1 AND m.id=:id"),
        @NamedQuery(name = Meal.MEAL_GETALL, query = "SELECT m FROM Meal m WHERE m.user.id=?1 ORDER BY m.dateTime DESC"),
        @NamedQuery(name = Meal.MEAL_GETALL_FILTERED, query = "SELECT m FROM Meal m WHERE m.user.id=?1 AND m.dateTime>=?2 AND m.dateTime<?3 ORDER BY m.dateTime DESC"),
        @NamedQuery(name = Meal.MEAL_DELETE, query = "DELETE FROM Meal m WHERE m.user.id=?1 AND m.id=?2"),
        @NamedQuery(name = Meal.MEAL_UPDATE, query = "UPDATE Meal m SET m.dateTime=?1, m.description=?2, m.calories=?3 WHERE m.id=?4 AND m.user.id=?5")
})
@Entity
@Table(name = "meals", uniqueConstraints = @UniqueConstraint(name = "meals_unique_datetime_idx", columnNames = {"user_id", "date_time"}))
public class Meal extends AbstractBaseEntity {
    public static final String MEAL_GET = "Meal.get";
    public static final String MEAL_GETALL = "Meal.getAll";
    public static final String MEAL_GETALL_FILTERED = "Meal.getAllFiltered";
    public static final String MEAL_DELETE = "Meal.delete";
    public static final String MEAL_UPDATE = "Meal.update";

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false)
    @Size(min = 2, max = 120, message = "Description length should be between 2 and 120 chars")
    @NotNull
    @NotBlank
    private String description;

    @Column(name = "calories", nullable = false)
    @Min(value = 10)
    @Max(value = 5000)
    @NotNull
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
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