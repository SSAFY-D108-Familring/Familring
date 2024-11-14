package com.familring.calendarservice.repository;

import com.familring.calendarservice.domain.Daily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyRepository extends JpaRepository<Daily, Long> {
    @Query("SELECT d FROM Daily d WHERE YEAR(d.createdAt) = :year AND MONTH(d.createdAt) = :month AND d.familyId = :familyId")
    List<Daily> findByYearAndMonthAndFamilyId(int year, int month, Long familyId);

    @Query("SELECT d FROM Daily d WHERE YEAR(d.createdAt) = :year AND MONTH(d.createdAt) = :month AND DAY(d.createdAt) = :day AND d.familyId = :familyId")
    List<Daily> findByDateAndFamilyId(int year, int month, int day, Long familyId);
}
