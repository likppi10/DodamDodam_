package com.ssafy.core.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.dto.res.AlarmResDto;
import com.ssafy.core.entity.Alarm;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.entity.QAlarm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlarmRepoCustomImpl implements AlarmRepoCustom{

    private final JPAQueryFactory jpaQueryFactory;
    QAlarm alarm = QAlarm.alarm;

    @Override
    public Alarm findAlarmByProfileAndTarget(Profile me, Profile target, String content) {
        return jpaQueryFactory.selectFrom(alarm)
                .where(alarm.me.eq(me)
                        .and(alarm.target.eq(target))
                        .and(alarm.content.eq(content)))
                .fetchFirst();
    }

    @Override
    public List<AlarmResDto> findAlarmByProfileAndTargetOrderByCount(Profile me, Profile target) {
        return jpaQueryFactory.select(Projections.fields(AlarmResDto.class,
                alarm.content))
                .from(alarm)
                .where(alarm.me.eq(me)
                        .and(alarm.target.eq(target)))
                .orderBy(alarm.count.desc())
                .fetch();
    }
}
