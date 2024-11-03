package com.familring.calendarservice.repository;

import com.familring.calendarservice.domain.Daily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.util.List;

@Repository
public interface DailyRepository extends JpaRepository<Daily, Long> {
    List<Daily> findDailiesByUserIdIn(List<Long> userIds);
}
