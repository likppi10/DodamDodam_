package com.ssafy.core.repository;

import com.ssafy.core.entity.Alarm;
import com.ssafy.core.repository.querydsl.AlarmRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long>, AlarmRepoCustom {
}
