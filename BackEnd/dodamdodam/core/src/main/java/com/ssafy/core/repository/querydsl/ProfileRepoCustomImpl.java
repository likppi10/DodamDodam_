package com.ssafy.core.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.dto.res.*;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.entity.QProfile;
import com.ssafy.core.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProfileRepoCustomImpl implements ProfileRepoCustom {
    private final JPAQueryFactory queryFactory;

    QProfile profile = QProfile.profile;
    QUser user = QUser.user;

    @Override
    public Profile findProfileByUserPk(Long userPK) {
        return queryFactory
                .selectFrom(profile)
                .where(profile.user.userPk.eq(userPK))
                .fetchFirst();
    }

    @Override
    public Long findProfileIdByUserPk(Long userPk) {
        return queryFactory
                .select(profile.id)
                .from(profile)
                .where(profile.user.userPk.eq(userPk))
                .fetchFirst();
    }

    @Override
    public List<MainProfileResDto> findProfileListByFamilyIdExceptMe(Long familyId, Long profileId) {
        return queryFactory
                .select(Projections.fields(MainProfileResDto.class,
                        profile.imagePath,
                        profile.role,
                        profile.emotion,
                        profile.comment,
                        profile.id.as("profileId")))
                .from(profile)
                .where(profile.family.id.eq(familyId).and(profile.id.ne(profileId)))
                .fetch();
    }

    @Override
    public ProfileIdAndFamilyIdResDto findProfileIdAndFamilyIdByUserPk(Long userPk) {
        return queryFactory
                .select(Projections.fields(ProfileIdAndFamilyIdResDto.class,
                        profile.id.as("profileId"),
                        profile.family.id.as("familyId")))
                .from(profile)
                .where(profile.user.userPk.eq(userPk))
                .fetchFirst();
    }

    @Override
    public Long checkRoleByFamilyIdExceptMe(Long familyId, String role, Long profileId) {
        return queryFactory.select(profile.id.count())
                .from(profile)
                .where(profile.family.id.eq(familyId),
                        profile.role.eq(role),
                        profileIdNotEquals(profileId))
                .fetchFirst();
    }

    @Override
    public Long checkNicknameByFamilyIdExceptMe(Long familyId, String nickname, Long profileId) {
        return queryFactory.select(profile.id.count())
                .from(profile)
                .where(profile.family.id.eq(familyId),
                        profile.nickname.eq(nickname),
                        profileIdNotEquals(profileId))
                .fetchFirst();
    }

    @Override
    public List<Profile> findProfilesByFamilyIdExceptMe(Long familyId, Long profileId) {
        return queryFactory.select(profile)
                .from(profile)
                .where(profile.family.id.eq(familyId),
                        profile.id.ne(profileId))
                .fetch();
    }

    @Override
    public MissionResDto findTodayMissionByUserPk(Long userPk) {
        return queryFactory.select(Projections.fields(MissionResDto.class,
                        profile.mission_content.as("missionContent"),
                        profile.mission_target.as("missionTarget")))
                .from(profile)
                .where(profile.user.userPk.eq(userPk))
                .fetchFirst();
    }

    @Override
    public List<ChattingMemberResDto> findChattingMemberListByFamilyId(Long familyId) {
        List<Long> ids = queryFactory
                .select(profile.id)
                .from(profile)
                .where(profile.family.id.eq(familyId))
                .fetch();

        return queryFactory
                .select(Projections.fields(ChattingMemberResDto.class,
                        profile.id.as("profileId"),
                        profile.imagePath.as("profileImage"),
                        profile.nickname))
                .from(profile)
                .where(profile.id.in(ids))
                .fetch();
    }

    @Override
    public String findFcmTokenByProfileId(Long profileId) {
        return queryFactory.select(user.fcmToken)
                .from(profile)
                .where(profile.id.eq(profileId))
                .fetchFirst();
    }


    private BooleanExpression profileIdNotEquals(Long profileId) {
        return profileId != null ? profile.id.ne(profileId) : null;
    }
}
