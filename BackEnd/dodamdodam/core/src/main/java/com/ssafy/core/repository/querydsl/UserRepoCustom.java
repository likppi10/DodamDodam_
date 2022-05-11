package com.ssafy.core.repository.querydsl;

import com.ssafy.core.dto.req.FindIdReqDto;
import com.ssafy.core.entity.Profile;

import java.time.LocalDate;

public interface UserRepoCustom {

    String findUserIdByUserInfo(String name, LocalDate parse, String familyCode);

    String findUserFcmTokenByProfile(Profile target);

    LocalDate findBirthdayByProfileId(Long profileId);
}
