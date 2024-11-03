package com.familring.calendarservice.repository;

import com.familring.calendarservice.domain.Daily;
import com.familring.calendarservice.domain.Schedule;
import com.familring.calendarservice.domain.ScheduleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleUserRepository extends JpaRepository<ScheduleUser, Long> {

}
