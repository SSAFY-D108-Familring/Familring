package com.familring.calendarservice.repository;

import com.familring.calendarservice.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT s FROM Schedule  s WHERE s.familyId = :familyId AND (MONTH(s.startTime) = :month OR MONTH(s.endTime) = :month)")
    List<Schedule> findSchedulesByMonthAndFamilyId(int month, Long familyId);
}
