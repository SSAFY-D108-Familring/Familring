package com.familring.calendarservice.repository;

import com.familring.calendarservice.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT DISTINCT s FROM Schedule s " +
            "JOIN s.scheduleUsers su " +
            "WHERE (MONTH(s.startTime) = :month OR MONTH(s.endTime) = :month) " +
            "AND su.userId IN :userIds")
    List<Schedule> findSchedulesByMonthAndUserIds(
            @Param("month") int month,
            @Param("userIds") List<String> userIds
    );
}
