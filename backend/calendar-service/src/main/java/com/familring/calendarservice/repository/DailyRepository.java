package com.familring.calendarservice.repository;

import com.familring.calendarservice.domain.Daily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.util.List;

@Repository
public interface DailyRepository extends JpaRepository<Daily, Long> {
    @Query("SELECT d FROM Daily d WHERE MONTH(d.createdAt) = :month AND d.userId IN :userIds")
    List<Daily> findDailiesByMonthAndUserIdIn(int month,  List<Long> userIds);
}
