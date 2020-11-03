package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    Meal findByIdAndUserId(int id, int userId);

    List<Meal> findAllByUserIdOrderByDateTimeDesc(int userId);

    @Transactional
    int deleteByIdAndUserId(int id, int userId);

    @Transactional
    @Modifying
    @Query("UPDATE Meal AS m SET m.dateTime=:time, m.description=:desc, m.calories=:cal WHERE m.id=:id AND m.user.id=:user_id")
    int update(@Param("time") LocalDateTime dateTime,
               @Param("desc") String description,
               @Param("cal") int calories,
               @Param("id") Integer id,
               @Param("user_id") int userId);

    @Query("SELECT m FROM Meal m JOIN FETCH m.user WHERE m.id=:id AND m.user.id=:user_id")
    Meal getMealWithUser(@Param("id") int id, @Param("user_id") int userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id=:user_id AND m.dateTime>=:start AND m.dateTime<:end ORDER BY m.dateTime DESC")
    List<Meal> getAllFiltered(@Param("start") LocalDateTime startDateTime,
                              @Param("end") LocalDateTime endDateTime,
                              @Param("user_id") int userId);
}
