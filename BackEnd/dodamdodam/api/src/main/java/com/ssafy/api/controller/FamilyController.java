package com.ssafy.api.controller;

import com.ssafy.core.dto.req.FamilyCreateReqDto;
import com.ssafy.core.dto.req.FamilyJoinReqDto;
import com.ssafy.core.dto.res.FamilyCodeResDto;
import com.ssafy.core.dto.res.ProfileIdAndFamilyIdResDto;
import com.ssafy.core.dto.res.FamilyIdResDto;
import com.ssafy.core.dto.res.FamilyPictureResDto;
import com.ssafy.core.entity.Family;
import com.ssafy.api.service.FamilyService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/family")
@Tag(name = "FamilyController", description = "가족컨트롤러")
public class FamilyController {

    private final FamilyService familyService;
    private final ResponseService responseService;

    @Operation(summary = "가족 그룹 생성", description = "<strong>가족 그룹</strong>을 생성한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SingleResult<ProfileIdAndFamilyIdResDto> createFamily(
            @ModelAttribute @Valid FamilyCreateReqDto familyReq,
            Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());
        return responseService.getSingleResult(familyService.createFamily(familyReq, userPk));
    }

    @Operation(summary = "가족 그룹 가입", description = "<strong>가족 그룹<strong>을 가입한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @PostMapping(value = "/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SingleResult<ProfileIdAndFamilyIdResDto> joinFamily(
            @ModelAttribute
            @Valid FamilyJoinReqDto familyRequest,
            Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());
        return responseService.getSingleResult(familyService.joinFamily(familyRequest, userPk));
    }

    @Operation(summary = "가족 코드 검사", description = "<strong>가족 코드<strong>를 받아 가족 id를 조회한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "/code/check/{code}")
    public SingleResult<FamilyIdResDto> checkFamilyCode(@PathVariable String code) {

        Family family = familyService.checkCode(code);
        FamilyIdResDto res = FamilyIdResDto.builder()
                .familyId(family.getId())
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "가족 코드 조회", description = "<strong>가족 id<strong>를 받아 가족 코드를 조회한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "/code")
    public SingleResult<FamilyCodeResDto> getFamilyCode(Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());
        Family family = familyService.fromUserIdToFamily(userPk);
        FamilyCodeResDto res = FamilyCodeResDto.builder()
                .code(family.getCode())
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "가족 사진 수정", description = "<strong>가족 사진<stong>을 추가 및 수정한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @PatchMapping(value = "/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult updateFamilyPicture(
            @RequestParam(value = "picture") MultipartFile picture, Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());
        Family family = familyService.fromUserIdToFamily(userPk);
        familyService.updateFamilyPicture(family, picture);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "가족 사진 경로 조회", description = "<strong>가족 사진 경로<stong>를 조회한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "/picture")
    public SingleResult<FamilyPictureResDto> getFamilyPicture(Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());
        Family family = familyService.fromUserIdToFamily(userPk);
        String picture = family.getPicture();
        FamilyPictureResDto res = FamilyPictureResDto.builder()
                .picture(picture)
                .build();
        return responseService.getSingleResult(res);
    }
}
