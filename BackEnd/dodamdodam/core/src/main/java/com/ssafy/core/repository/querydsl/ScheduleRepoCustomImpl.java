package com.ssafy.core.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.dto.res.ScheduleDetailResDto;
import com.ssafy.core.entity.Family;
import com.ssafy.core.entity.QSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleRepoCustomImpl implements ScheduleRepoCustom{

    private final JPAQueryFactory queryFactory;
    QSchedule schedule = QSchedule.schedule;
    @Override
    public List<ScheduleDetailResDto> findScheduleByMonth(Integer month, Family family) {
        return queryFactory
                .select(Projections.fields(ScheduleDetailResDto.class,
                        schedule.id.as("scheduleId"),
                        schedule.title,
                        schedule.content,
                        schedule.startDate,
                        schedule.endDate,
                        schedule.role))
                .from(schedule)
                .where(schedule.family.eq(family)
                        .and((schedule.startDate.month().loe(month))
                                .and(schedule.endDate.month().goe(month)))
                )
                .fetch();
    }

    @Override
    public List<ScheduleDetailResDto> findScheduleByDay(LocalDate day, Family family) {
        return queryFactory
                .select(Projections.fields(ScheduleDetailResDto.class,
                        schedule.id.as("scheduleId"),
                        schedule.title,
                        schedule.content,
                        schedule.startDate,
                        schedule.endDate,
                        schedule.role))
                .from(schedule)
                .where(schedule.family.eq(family)
                        .and((schedule.startDate.loe(day)
                                .and(schedule.endDate.goe(day)))))
                .fetch();

    }
}
