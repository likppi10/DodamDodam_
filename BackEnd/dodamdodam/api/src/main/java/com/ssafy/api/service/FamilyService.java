package com.ssafy.api.service;

import com.ssafy.core.common.FileUtil;
import com.ssafy.core.dto.req.FamilyCreateReqDto;
import com.ssafy.core.dto.req.FamilyJoinReqDto;
import com.ssafy.core.entity.Family;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.entity.User;
import com.ssafy.core.exception.ErrorCode;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.FamilyRepository;
import com.ssafy.core.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.Random;

import static com.ssafy.core.common.FileUtil.FILE_MAX_SIZE;

@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyService {
    private final FamilyRepository familyRepository;
    private final ProfileRepository profileRepository;
    private final FileService fileService;
    private final Random random = new SecureRandom();

    // 가족 생성 및 프로필 생성
    public Family createFamily() {
        String key;
        for (int i = 0; true; i++) {
            key = "";
            for (int j = 0; j < 15; j++) {
                if (random.nextBoolean()) {
                    key += ((char) ((int) (random.nextInt(26)) + 65));
                } else {
                    key += (random.nextInt(10));
                }
            }
            if (familyRepository.findFamilyByCode(key) == null) {
                break;
            }
        }
        return familyRepository.save(Family.builder()
                .code(key)
                .build());
    }

    // profile 생성
    public Profile createProfileForFirst(Family family, User user, FamilyCreateReqDto familyRequest, MultipartFile file) {
        Profile profile = Profile.builder()
                .role(familyRequest.getRole())
                .nickname(familyRequest.getNickname())
                .user(user)
                .family(family)
                .build();

        profileRepository.save(profile);
        if (file.getSize() > FILE_MAX_SIZE) {
            fileService.resizeImage("profile", file, profile);
        }
        return profile;
    }

    public Profile createProfileForJoin(Family family, User user, FamilyJoinReqDto familyRequest, MultipartFile file) {
        Profile profile = Profile.builder()
                .role(familyRequest.getRole())
                .nickname(familyRequest.getNickname())
                .user(user)
                .family(family)
                .build();

        profileRepository.save(profile);
        if (file.getSize() > FILE_MAX_SIZE) {
            fileService.resizeImage("profile", file, profile);
        }
        return profile;
    }

    // family_id로 Family 객체 조회
    public Family getFamily(long familyId) {
        Family family = familyRepository.findFamilyById(familyId);
        if (family == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "해당 그룹이 없습니다.");
        }
        return family;
    }

    // code로 family 조회
    public Family checkCode(String code) {
        Family family = familyRepository.findFamilyByCode(code);
        if (family == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "해당 그룹이 없습니다.");
        }
        return family;
    }


    public void familyExistCheck(Long userPK) {
        if (profileRepository.findProfileByUserPk(userPK) != null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "이미 가입된 그룹이 있습니다.");
        }
    }

    public Family fromUserIdToFamily(Long userPk) {

        Profile profile = profileRepository.findProfileByUserPk(userPk);
        if (profile == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "소속된 그룹이 없습니다.");
        }
        long familyId = profile.getFamily().getId();
        return familyRepository.findFamilyById(familyId);
    }

    public void updateFamilyPicture(Family family, MultipartFile picture) {

        String filePath = fileService.uploadFileV1("family", picture);
        family.setPicture(filePath);
        familyRepository.save(family);
        if (picture.getSize() > FILE_MAX_SIZE) {
            fileService.resizeImage("family", picture, family);
        }
    }
}
