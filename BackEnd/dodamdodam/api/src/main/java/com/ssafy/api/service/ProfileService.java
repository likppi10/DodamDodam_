package com.ssafy.api.service;

import com.ssafy.core.common.KoreanUtil;
import com.ssafy.core.common.MissionList;
import com.ssafy.core.dto.req.ProfileReqDto;
import com.ssafy.core.dto.req.StatusReqDto;
import com.ssafy.core.dto.res.ChattingMemberResDto;
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

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.ssafy.core.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final FileService fileService;
    private final MainService mainService;
    private final Random random = new Random();
    final Map<String, String[][]> missionList = MissionList.missionList;

    //[나][대상]
    final String[][] call = {
            {"형", "누나"},
            {"오빠", "언니"}
    };


    @Transactional(readOnly = false)
    public void enrollProfile(Profile profile) {
        profileRepository.save(profile);
    }

    //    @Transactional(readOnly = false)
//    public void enrollProfile(String userId){
//        User user = userService.findByUserId(userId);
//
//        String[] imageInfo  = enrollImage(multipartFile, request).split("#");
//        //family코드로 넣을 부분 필요
//        Profile profile = Profile.builder()
//                .role(profileRequest.getRole())
//                .nickname(profileRequest.getNickname())
//                .user(user)
//                .imagePath(imageInfo[0])
//                .imageName(imageInfo[1])
////                .family()
//                .build();
//
//        profileService.enrollProfile(profile);
//
//    }
    @Transactional(readOnly = false)
    public Profile updateProfile(Long userPK, ProfileReqDto profileDto, MultipartFile multipartFile, HttpServletRequest request) {
        Profile profile = profileRepository.findProfileByUserPk(userPK);
        String originFileName = multipartFile.getOriginalFilename();
        String filePath = fileService.uploadFileV1("profile", multipartFile);
        profile.updateImageName(originFileName);
        profile.updateImagePath(filePath);
//        updateImage(multipartFile, profile, request);
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

        if (profile.getMission_target() == null) {
            //본인을 제외한 가족 profiles 가져오기
            List<Profile> familyProfiles = profileRepository.findProfilesByFamilyIdExceptMe(familyId, profile.getId());

            //본인 뿐이 없다면, target은 비우고, content만 갱신
            if (familyProfiles.size() == 0) {
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

            String missionTargetCall = "";
            //본인, 미션 대상이 엄마 아빠가 아니라면
            if (!(profile.getRole().equals("엄마") || profile.getRole().equals("아빠")
                    || targetRole.equals("엄마") || targetRole.equals("아빠"))) {
                LocalDate targetBirthday = userRepository.findBirthdayByProfileId(missionTarget.getId());
                LocalDate myBirthday = userRepository.findBirthdayByProfileId(profile.getId());

                if (targetBirthday.isAfter(myBirthday)) {
                    int myGender = profile.getRole().equals("아들") ? 0 : 1;
                    int targetGender = targetRole.equals("아들") ? 0 : 1;
                    targetRole = call[myGender][targetGender];
                } else {
                    targetRole = "동생";
                }
                missionTargetCall = missionTargetCall.concat(targetRole + "(" + missionTarget.getNickname() + ")");
            } else {
                missionTargetCall = missionTargetCall.concat(targetRole);
            }

            //0번인지 1번인지 정하기
            int randomIndex = random.nextInt(2);
            String missionSelect = missions[randomIndex][random.nextInt(missions[randomIndex].length)];

            StringBuilder missionContent = new StringBuilder();

            //미션 대상 호칭 선택
            switch (randomIndex) {
                case 0:
                    missionContent.append(missionTargetCall);
                    missionContent.append("에게 ");
                    break;
                case 1:
                    String caseParticle = KoreanUtil.getCompleteWord(targetRole, "을 ", "를 ");
                    missionContent.append(caseParticle);
                    break;
            }

            missionContent.append(missionSelect);

            profile.updateMissionContent(missionContent.toString());
        }
        return profile;
    }


    @Transactional(readOnly = false)
    public void updateStatus(Profile profile, StatusReqDto statusDto) {
        profile.updateEmotion(statusDto.getEmotion());
        profile.updateComment(statusDto.getComment());

        profileRepository.save(profile);
    }
//
//    @Transactional(readOnly = false)
//    public void updateImage(MultipartFile multipartFile, Profile profile, HttpServletRequest request){
//        try{
//            String separ = File.separator;
//            String today= new SimpleDateFormat("yyMMdd").format(new Date());
//
//            File file = new File("");
////            String rootPath = file.getAbsolutePath().split("src")[0];
//
////            String savePath = "../"+"profileImg"+separ+today;
//            String savePath = request.getServletContext().getRealPath("/resources/profileImage");
//            log.info(savePath);
//            if(!new File(savePath).exists()){
//                try{
//                    new File(savePath).mkdirs();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//            String originFileName = multipartFile.getOriginalFilename();
//            String saveFileName = UUID.randomUUID().toString() + originFileName.substring(originFileName.lastIndexOf("."));
//
//            String filePath = savePath+separ+saveFileName;
//            multipartFile.transferTo(new File(filePath));
//            profile.updateImagePath("/resources/profileImage/"+saveFileName);
//            profile.updateImageName(originFileName);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    @Transactional(readOnly = false)
    public String enrollImage(MultipartFile multipartFile, HttpServletRequest request) {

        try {
            if (!multipartFile.isEmpty()) {
                String originFileName = multipartFile.getOriginalFilename();
                String filePath = fileService.uploadFileV1("profile", multipartFile);
                return filePath + "#" + originFileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ".#.";
    }

    @Transactional
    public String findImage(Long userPk) {
        Profile profile = profileRepository.findProfileByUserPk(userPk);
        return profile.getImagePath();
    }

    @Transactional
    public Profile findProfileByUserPk(Long userPk) {
        Profile profile = profileRepository.findProfileByUserPk(userPk);
        return profile;
    }


    public void checkRoleByFamilyIdExceptMe(Long familyId, String role, Long userPk) {
        Long profileId = profileRepository.findProfileIdByUserPk(userPk);
        if (profileRepository.checkRoleByFamilyIdExceptMe(familyId, role, profileId) != 0) {
            throw new CustomException(DUPLICATE_ROLE);
        }
    }

    public void checkNicknameByFamilyIdExceptMe(Long familyId, String nickname, Long userPk) {
        Long profileId = profileRepository.findProfileIdByUserPk(userPk);
        if (profileRepository.checkNicknameByFamilyIdExceptMe(familyId, nickname, profileId) != 0) {
            throw new CustomException(DUPLICATE_NICKNAME);
        }
    }

    @Transactional(readOnly = true)
    public List<ChattingMemberResDto> getProfileListByUserPk(long userPk) {

        Long familyId = familyRepository.findFamilyIdByUserPk(userPk);

        if (profileRepository.findChattingMemberListByFamilyId(familyId) == null) {
            throw new CustomException(INVALID_REQUEST);
        }

        return profileRepository.findChattingMemberListByFamilyId(familyId);
    }

    @Transactional
    public void deleteProfile(long userPk){
        Profile profile = findProfileByUserPk(userPk);
        List<MainProfileResDto> familyProfileList = mainService.getProfileList(userPk);
        if(familyProfileList.isEmpty()){
            Family family = familyRepository.findFamilyByUserPk(userPk);
            familyRepository.delete(family);
        }
        profileRepository.delete(profile);
    }
}
