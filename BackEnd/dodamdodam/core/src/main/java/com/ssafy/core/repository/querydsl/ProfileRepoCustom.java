package com.ssafy.core.repository.querydsl;

import com.ssafy.core.dto.res.*;
import com.ssafy.core.entity.Profile;

import java.util.List;

public interface ProfileRepoCustom {

    Profile findProfileByUserPk(Long userPK);

    Long findProfileIdByUserPk(Long userPk);

    List<MainProfileResDto> findProfileListByFamilyIdExceptMe(Long familyId, Long profileId);

    ProfileIdAndFamilyIdResDto findProfileIdAndFamilyIdByUserPk(Long userPk);

    Long checkRoleByFamilyIdExceptMe(Long familyId, String role, Long profileId);

    Long checkNicknameByFamilyIdExceptMe(Long familyId, String nickname, Long profileId);

    List<Profile> findProfilesByFamilyIdExceptMe(Long familyId, Long profileId);

    MissionResDto findTodayMissionByUserPk(Long userPk);

    List<ChattingMemberResDto> findChattingMemberListByFamilyId(Long familyId);

    String findFcmTokenByProfileId(Long profileId);
}
