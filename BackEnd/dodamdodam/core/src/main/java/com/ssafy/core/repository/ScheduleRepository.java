package com.ssafy.core.repository;

import com.ssafy.core.entity.Schedule;
import com.ssafy.core.repository.querydsl.ScheduleRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepoCustom {
    Schedule findScheduleById(long scheduleId);
}
