package com.ssafy.api.service;

import com.ssafy.core.common.FileUtil;
import com.ssafy.core.common.KoreanUtil;
import com.ssafy.core.common.MissionListUtil;
import com.ssafy.core.dto.req.ProfileReqDto;
import com.ssafy.core.dto.req.StatusReqDto;
import com.ssafy.core.dto.res.ChattingMemberResDto;
import com.ssafy.core.dto.res.MyProfileResDto;
import com.ssafy.core.dto.res.TodayConditionResDto;
import com.ssafy.core.dto.res.MainProfileResDto;
import com.ssafy.core.entity.Family;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.FamilyRepository;
import com.ssafy.core.repository.ProfileRepository;
import com.ssafy.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.ssafy.core.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final FileService fileService;
    private final MainService mainService;
    private final Random random = new SecureRandom();
    static final Map<String, String[][]> missionList = MissionListUtil.missionList;


    @Transactional
    public void enrollProfile(Profile profile) {
        profileRepository.save(profile);
    }

    @Transactional
    public Profile updateProfile(Long userPK, ProfileReqDto profileDto, MultipartFile multipartFile, String characterPath) {
        Profile profile = profileRepository.findProfileByUserPk(userPK);

        if (characterPath != null) {
            String originFileName = characterPath.substring(characterPath.lastIndexOf('/') + 1).toLowerCase();

            profile.updateImageName(originFileName);
            profile.updateImagePath(characterPath);

        } else if (multipartFile != null) {
            String originFileName = multipartFile.getOriginalFilename();
            String filePath = fileService.uploadFileV1("profile", multipartFile);

            profile.updateImageName(originFileName);
            profile.updateImagePath(filePath);
            if (multipartFile.getSize() > FileUtil.FILE_MAX_SIZE) {
                fileService.resizeImage(multipartFile, profile);
            }

        } else {
            profile.updateImageName(null);
            profile.updateImagePath(null);
        }

        profile.updateRole(profileDto.getRole());
        profile.updateNickname(profileDto.getNickname());

        return profile;
    }

    @Transactional
    public Profile createMission(Long userPk) {

        //본인 프로필
        Profile profile = profileRepository.findProfileByUserPk(userPk);

        //본인 가족 Id
        Long familyId = familyRepository.findFamilyIdByUserPk(userPk);

        //아직 Mission Target이 없으면 mission 생성
        if (profile.getMission_target() == null) {
            //본인을 제외한 가족 profiles 가져오기
            List<Profile> familyProfiles = profileRepository.findProfilesByFamilyIdExceptMe(familyId, profile.getId());

            //본인 뿐이 없다면, target은 비우고, content만 갱신
            if (familyProfiles.isEmpty()) {
                profile.updateMissionContent("우리 가족을 초대해 주세요!");
                return profile;
            }

            //미션 대상 선정.
            int targetNumber = random.nextInt(familyProfiles.size());
            Profile missionTarget = familyProfiles.get(targetNumber);
            String targetRole =
                    missionTarget.getRole().length() > 2 ? missionTarget.getRole().split(" ")[1] : missionTarget.getRole();

            //targetRole Update
            profile.updateMissionTarget(targetRole);

            //미션 대상에 맞는 미션 선정
            String[][] missions = missionList.get(targetRole);

            //미션 정하기 : 0번 -> 에게, 1번 -> 을/를, 2번 -> 와
            int randomIndex = random.nextInt(3);
            String missionSelect = missions[randomIndex][random.nextInt(missions[randomIndex].length)];

            StringBuilder missionContent = new StringBuilder();

            //미션 대상 호칭 선택
            if (randomIndex == 0) {
                missionContent.append(targetRole);
                missionContent.append("에게 ");
            } else if (randomIndex == 1) {
                String caseParticle = KoreanUtil.getCompleteWord(targetRole, "을 ", "를 ");
                missionContent.append(caseParticle);
            }else{
                String caseParticle = KoreanUtil.getCompleteWord(targetRole, "과 ", "와 ");
                missionContent.append(caseParticle);
            }

            missionContent.append(missionSelect);
            profile.updateMissionContent(missionContent.toString());
        }
        return profile;
    }


    @Transactional
    public void updateStatus(Profile profile, StatusReqDto statusDto) {
        profile.updateEmotion(statusDto.getEmotion());
        profile.updateComment(statusDto.getComment());

        profileRepository.save(profile);
    }

    @Transactional
    public String findImage(Long userPk) {
        Profile profile = profileRepository.findProfileByUserPk(userPk);
        return profile.getImagePath();
    }

    @Transactional
    public Profile findProfileByUserPk(Long userPk) {
        return profileRepository.findProfileByUserPk(userPk);
    }


    @Transactional(readOnly = true)
    public void checkRoleByFamilyIdExceptMe(Long familyId, String role, Long userPk) {

        Long profileId = profileRepository.findProfileIdByUserPk(userPk);
        if (profileRepository.checkRoleByFamilyIdExceptMe(familyId, role, profileId) != 0) {
            throw new CustomException(DUPLICATE_ROLE);
        }
    }

    @Transactional(readOnly = true)
    public void checkNicknameByFamilyIdExceptMe(Long familyId, String nickname, Long userPk) {

        Long profileId = profileRepository.findProfileIdByUserPk(userPk);
        if (profileRepository.checkNicknameByFamilyIdExceptMe(familyId, nickname, profileId) != 0) {
            throw new CustomException(DUPLICATE_NICKNAME);
        }
    }

    @Transactional(readOnly = true)
    public List<ChattingMemberResDto> getProfileListByUserPk(Long userPk) {

        Long familyId = familyRepository.findFamilyIdByUserPk(userPk);

        List<ChattingMemberResDto> chattingProfileList = profileRepository.findChattingMemberListByFamilyId(familyId);
        if (chattingProfileList == null) {
            throw new CustomException(INVALID_REQUEST);
        }
        return chattingProfileList;
    }

    @Transactional(readOnly = true)
    public TodayConditionResDto getTodayCondition(Long userPk) {

        Profile myProfile = profileRepository.findProfileByUserPk(userPk);
        if (myProfile == null) {
            throw new CustomException(NOT_FOUND_FAMILY);
        }
        return TodayConditionResDto.builder()
                .comment(myProfile.getComment())
                .emotion(myProfile.getEmotion())
                .build();
    }

    @Transactional(readOnly = true)
    public MyProfileResDto getMyProfile(Long userPk) {

        Profile myProfile = profileRepository.findProfileByUserPk(userPk);
        if (myProfile == null) {
            throw new CustomException(NOT_FOUND_FAMILY);
        }

        LocalDate birthday = userRepository.findBirthdayByProfileId(myProfile.getId());

        if (birthday == null) {
            throw new CustomException(NOT_FOUND_FAMILY);
        }

        return MyProfileResDto.builder()
                .role(myProfile.getRole())
                .nickname(myProfile.getNickname())
                .birthday(birthday)
                .imagePath(myProfile.getImagePath())
                .build();
    }

    @Transactional
    public void deleteProfile(Long userPk) {
        Profile profile = findProfileByUserPk(userPk);
        List<MainProfileResDto> familyProfileList = mainService.getProfileListExceptMe(userPk);
        if (familyProfileList.isEmpty()) {
            Family family = familyRepository.findFamilyByUserPk(userPk);
            familyRepository.delete(family);
        }
        profileRepository.delete(profile);
    }
}
