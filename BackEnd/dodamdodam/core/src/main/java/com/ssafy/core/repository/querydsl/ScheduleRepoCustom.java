package com.ssafy.core.repository.querydsl;

import com.ssafy.core.dto.res.ScheduleDetailResDto;
import com.ssafy.core.entity.Family;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepoCustom {
    List<ScheduleDetailResDto> findScheduleByMonth(Integer month, Family family);
    List<ScheduleDetailResDto> findScheduleByDay(LocalDate day, Family family);
}
