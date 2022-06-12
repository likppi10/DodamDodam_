package com.ssafy.api.service;

import com.ssafy.core.common.FamilyCodeUtil;
import com.ssafy.core.dto.req.FamilyCreateReqDto;
import com.ssafy.core.dto.req.FamilyJoinReqDto;
import com.ssafy.core.dto.res.ProfileIdAndFamilyIdResDto;
import com.ssafy.core.entity.Family;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.entity.User;
import com.ssafy.core.exception.ErrorCode;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.FamilyRepository;
import com.ssafy.core.repository.ProfileRepository;
import com.ssafy.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.ssafy.core.common.FileUtil.FILE_MAX_SIZE;
import static com.ssafy.core.exception.ErrorCode.USER_DOESNT_EXIST;

@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyService {
    private final FamilyRepository familyRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    private final ProfileService profileService;
    private final FileService fileService;
    private final FamilyCodeUtil familyCodeUtil;

    // 가족 생성 및 프로필 생성
    @Transactional
    public ProfileIdAndFamilyIdResDto createFamily(FamilyCreateReqDto request, Long userPk) {

        //1. 다른 가족 가입 여부 확인
        this.familyExistCheck(userPk);

        //2. FamilyCode 생성
        String familyKey = familyCodeUtil.createFamilyCode();
        Family family = familyRepository.save(Family.builder()
                .code(familyKey)
                .build());

        //3. 유저 생일 업데이트
        User user = userRepository.findUserByUserPk(userPk)
                .orElseThrow(() -> new CustomException(USER_DOESNT_EXIST));

        user.updateBirthday(request.getBirthday());
        userRepository.save(user);

        //4. 프로필 생성, Family 가입
        Profile profile = this.createProfile(
                family,
                user,
                request.getRole(),
                request.getNickname(),
                request.getImage(),
                request.getCharacterPath()
        );

        return ProfileIdAndFamilyIdResDto.builder()
                .familyId(family.getId())
                .profileId(profile.getId())
                .build();
    }

    @Transactional
    public ProfileIdAndFamilyIdResDto joinFamily(FamilyJoinReqDto request, Long userPk) {
        //1. 다른 가족 가입 여부 확인
        this.familyExistCheck(userPk);

        //2. 유저 생일 업데이트
        User user = userRepository.findUserByUserPk(userPk)
                .orElseThrow(() -> new CustomException(USER_DOESNT_EXIST));

        user.updateBirthday(request.getBirthday());
        userRepository.save(user);

        //3. Role 중복 검사
        profileService.checkRoleByFamilyIdExceptMe(request.getFamilyId(), request.getRole(), userPk);
        //4. NickName 중복 검사
        profileService.checkNicknameByFamilyIdExceptMe(request.getFamilyId(), request.getNickname(), userPk);

        //5. 프로필 생성, Family 가입
        Family family = this.getFamily(request.getFamilyId());
        Profile profile = this.createProfile(
                family,
                user,
                request.getRole(),
                request.getNickname(),
                request.getImage(),
                request.getCharacterPath()
        );

        return ProfileIdAndFamilyIdResDto.builder()
                .familyId(family.getId())
                .profileId(profile.getId())
                .build();
    }

    // profile 생성
    @Transactional
    public Profile createProfile(Family family, User user, String role, String nickName, MultipartFile file, String characterPath) {
        String imagePath = null;
        String imageName = null;

        if (characterPath != null) {
            imageName = characterPath.substring(characterPath.lastIndexOf('/') + 1).toLowerCase();
            imagePath = characterPath;
        } else if (file != null) {
            imageName = file.getOriginalFilename();
            imagePath = fileService.uploadFileV1("profile", file);
        }

        Profile profile = Profile.builder()
                .role(role)
                .nickname(nickName)
                .user(user)
                .family(family)
                .imageName(imageName)
                .imagePath(imagePath)
                .build();

        profile = profileRepository.save(profile);
        if (file != null && file.getSize() > FILE_MAX_SIZE) {
            fileService.resizeImage(file, profile);
        }
        return profile;
    }

    // family_id로 Family 객체 조회
    @Transactional(readOnly = true)
    public Family getFamily(long familyId) {

        Family family = familyRepository.findFamilyById(familyId);
        if (family == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "해당 그룹이 없습니다.");
        }
        return family;
    }

    // code로 family 조회
    @Transactional(readOnly = true)
    public Family checkCode(String code) {

        Family family = familyRepository.findFamilyByCode(code);
        if (family == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "해당 그룹이 없습니다.");
        }
        return family;
    }

    @Transactional(readOnly = true)
    public void familyExistCheck(Long userPK) {

        if (profileRepository.findProfileByUserPk(userPK) != null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "이미 가입된 그룹이 있습니다.");
        }
    }

    @Transactional(readOnly = true)
    public Family fromUserIdToFamily(Long userPk) {

        Profile profile = profileRepository.findProfileByUserPk(userPk);
        if (profile == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "소속된 그룹이 없습니다.");
        }
        Long familyId = profile.getFamily().getId();
        return familyRepository.findFamilyById(familyId);
    }

    @Transactional
    public void updateFamilyPicture(Family family, MultipartFile picture) {

        String filePath = fileService.uploadFileV1("family", picture);
        family.setPicture(filePath);
        familyRepository.save(family);

        if (picture.getSize() > FILE_MAX_SIZE) {
            fileService.resizeImage(picture, family);
        }
    }

}
