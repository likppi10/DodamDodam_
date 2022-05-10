package com.ssafy.api.service;

import com.ssafy.core.common.MissionList;
import com.ssafy.core.dto.req.ProfileReqDto;
import com.ssafy.core.dto.req.StatusReqDto;
import com.ssafy.core.dto.res.ChattingMemberResDto;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.entity.User;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.exception.ErrorCode;
import com.ssafy.core.repository.FamilyRepository;
import com.ssafy.core.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static com.ssafy.core.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final FamilyRepository familyRepository;
    private final FileService fileService;
    private final Random random = new Random();
    private final Map<String, String[]> missionList = MissionList.missionList;


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
            String missionTarget = familyProfiles.get(targetNumber).getRole();
            profile.updateMissionTarget(missionTarget);

            //미션 대상에 맞는 미션 선정
            String[] missions = missionList.get(
                    missionTarget.length() > 2 ? missionTarget.split(" ")[1] : missionTarget);
            String missionContent = missions[random.nextInt(missions.length)];
            profile.updateMissionContent(missionContent);

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

        if(profileRepository.findChattingMemberListByFamilyId(familyId) == null){
            throw new CustomException(INVALID_REQUEST);
        }

        return profileRepository.findChattingMemberListByFamilyId(familyId);
    }
}
