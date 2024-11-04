package com.familring.calendarservice.repository;

import com.familring.calendarservice.domain.Daily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyRepository extends JpaRepository<Daily, Long> {
    @Query("SELECT d FROM Daily d WHERE YEAR(d.createdAt) = :year AND MONTH(d.createdAt) = :month AND d.familyId = :familyId")
    List<Daily> findDailiesByDateAndFamilyId(int year, int month, Long familyId);
}
