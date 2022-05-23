package com.ssafy.core.repository.querydsl;

import com.ssafy.core.dto.res.AlarmResDto;
import com.ssafy.core.entity.Alarm;
import com.ssafy.core.entity.Profile;

import java.util.List;

public interface AlarmRepoCustom {
    Alarm findAlarmByProfileAndTarget(Profile me, Profile target, String content);

    List<AlarmResDto> findAlarmByProfileAndTargetOrderByCount(Profile me, Profile target);
}