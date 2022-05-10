package com.ssafy.api.controller;

import com.ssafy.core.dto.req.ProfileReqDto;
import com.ssafy.core.dto.req.StatusReqDto;
import com.ssafy.core.entity.Profile;
import com.ssafy.api.service.ProfileService;
import com.ssafy.api.service.UserService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/profile")
@Tag(name = "ProfileController", description = "프로필 컨트롤러")
public class ProfileController {
    private final ResponseService responseService;
    private final ProfileService profileService;
    private final UserService userService;


//    @Operation(summary = "프로필 등록", description = "<strong>프로필 등록</strong>",
//            parameters = {
//                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
//            })
//    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public CommonResult enrollProfile(@RequestBody
//                                      @io.swagger.v3.oas.annotations.parameters.RequestBody
//                                      @Valid ProfileReqDto profileRequest,
//                                      @RequestParam(value = "file", required = false) MultipartFile multipartFile,
//                                      Authentication authentication,
//                                      HttpServletRequest request) {
//
//        User user = userService.findByUserPk(Long.parseLong(authentication.getName()));
//
//        userService.updateBirthdayWithUserPk(user.getUserPk(), profileRequest.getBirthday());
//
//        String[] imageInfo = profileService.enrollImage(multipartFile, request).split("#");
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
//        return responseService.getSuccessResult();
//    }


    @Operation(summary = "프로필 수정", description = "<strong>프로필 수정</strong>",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @PatchMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult updateProfile(@ModelAttribute
                                      @Valid ProfileReqDto profileRequest,
                                      Authentication authentication,
                                      HttpServletRequest request) {

        Long userPk = Long.parseLong(authentication.getName());

        Long familyId = userService.getFamilyIdByUserPk(userPk);

        profileService.checkNicknameByFamilyIdExceptMe(familyId, profileRequest.getNickname(), userPk);
        profileService.checkRoleByFamilyIdExceptMe(familyId, profileRequest.getRole(), userPk);

        Profile updateResult = profileService.updateProfile(userPk, profileRequest, profileRequest.getMultipartFile(), request);

        userService.updateBirthdayWithUserPk(userPk, profileRequest.getBirthday());

        profileService.enrollProfile(updateResult);

        return responseService.getSuccessResult();
    }

//    @Operation(summary = "미션 등록", notes = "<strong>미션 등록</strong>",
//            parameters = {
//                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
//            })
//    @PatchMapping("/mission")
//    public CommonResult updateMission(@RequestBody @Valid MissionReqDto missionReqDto, Authentication authentication) {
//        String token = jwtTokenProvider.resolveToken(request);
//        String userId = jwtTokenProvider.getUserId(token);
//        Profile mission = profileService.enrollMission(userId, missionReqDto.getMissionContent());
//        profileService.enrollProfile(mission);
//
//        return responseService.getSuccessResult();
//    }


    @Operation(summary = "상태 수정", description = "<strong>상태 수정</strong>",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @PatchMapping(value = "/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult updateStatus(@RequestBody
                                     @io.swagger.v3.oas.annotations.parameters.RequestBody
                                     @Valid StatusReqDto statusReqDto, Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());

        Profile checkMissionProfile = profileService.createMission(userPk);

        profileService.updateStatus(checkMissionProfile, statusReqDto);

        return responseService.getSuccessResult();
    }

    @Operation(summary = "프로필이미지 조회", description = "<strong>프로필 이미지 조회</strong>",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "/image")
    public SingleResult<String> getProfileImage(Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());
        String imagePath = profileService.findImage(userPk);
        return responseService.getSingleResult(imagePath);
    }

    @Operation(summary = "오늘의 한마디 조회", description = "<strong>오늘의 한마디 조회</strong>",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "/comment")
    public SingleResult<String> getComment(Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());
        Profile profile = profileService.findProfileByUserPk(userPk);
        return responseService.getSingleResult(profile.getComment());
    }

}
