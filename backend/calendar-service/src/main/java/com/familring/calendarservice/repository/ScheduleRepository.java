package com.familring.calendarservice.repository;

import com.familring.calendarservice.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT s FROM Schedule  s WHERE s.familyId = :familyId" +
            " AND (YEAR(s.startTime) = :year OR YEAR(s.endTime) = :year)" +
            " AND (MONTH(s.startTime) = :month OR MONTH(s.endTime) = :month)")
    List<Schedule> findByYearAndMonthAndFamilyId(int year, int month, Long familyId);

    @Query("SELECT s FROM Schedule s WHERE s.familyId = :familyId" +
            " AND s.startTime <= :endOfDay" +
            " AND s.endTime >= :startOfDay")
    List<Schedule> findByDateAndFamilyId(LocalDateTime startOfDay, LocalDateTime endOfDay, Long familyId);
}
