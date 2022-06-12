package com.ssafy.core.repository.querydsl;

import com.ssafy.core.common.ProviderType;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.entity.User;

import java.time.LocalDate;

public interface UserRepoCustom {

    String findUserIdByUserInfo(String name, LocalDate parse, String familyCode);

    String findUserFcmTokenByProfile(Profile target);

    LocalDate findBirthdayByProfileId(Long profileId);

    User findUserByUserIdAndProviderType(String userId, ProviderType providerType);
}
