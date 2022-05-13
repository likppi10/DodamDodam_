package com.ssafy.api.controller;

import com.ssafy.core.dto.req.*;
import com.ssafy.core.dto.res.ReIssueTokenResDto;
import com.ssafy.core.dto.res.SignInResDto;
import com.ssafy.core.entity.User;
import com.ssafy.core.exception.CustomException;
import com.ssafy.api.service.UserService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ssafy.core.exception.ErrorCode.*;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "UserController", description = "유저 컨트롤러")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    final int USER_ID_MAX = 20;
    final int USER_ID_MIN = 4;

    @GetMapping(value = "{userId}")
    @Operation(summary = "ID 중복체크", description = "<strong>아이디</strong>의 사용여부를 확인한다.")
    public CommonResult idCheck(@PathVariable String userId) {

        if (this.idValidate(userId)) {
            userService.checkExistId(userId);
        } else {
            throw new CustomException(INVALID_REQUEST,
                    String.format("아이디는 영문,숫자만 사용가능하며, %d자 이상, %d자 이하여야 합니다.", USER_ID_MIN, USER_ID_MAX));
        }

        return responseService.getSuccessResult("사용 가능한 아이디입니다.");
    }


    @PostMapping(value = "signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원 가입", description = "<strong>아이디, 패스워드, 이름</strong> 정보를 받아 회원가입 한다.")
    public CommonResult userSignUp
            (@RequestBody
             @io.swagger.v3.oas.annotations.parameters.RequestBody
             @Valid SignUpReqDto singUpRequest) {

        userService.signUp(singUpRequest);

        return responseService.getSuccessResult("회원가입 성공");
    }

    @PostMapping(value = "signin", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "로그인", description = "<strong>아이디, 패스워드</strong> 정보를 받아 로컬 로그인 한다.")
    public SingleResult<SignInResDto> userSignIn
            (@RequestBody
             @io.swagger.v3.oas.annotations.parameters.RequestBody
             @Valid UserInfoReqDto signInRequest) {

        return responseService.getSingleResult(userService.localSignIn(signInRequest));
    }

    @Operation(summary = "소셜 로그인", description = "<strong>소셜 로그인<strong> 한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", required = true, in = HEADER)
            })
    @PostMapping(value = "social", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody SingleResult<SignInResDto> socialLogin(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken) {
        SignInResDto signInResDto = userService.socialSignIn(accessToken);

        return responseService.getSingleResult(signInResDto);
    }


    @PostMapping(value = "refresh")
    @Parameters({
            @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER),
            @Parameter(name = "X-AUTH-REFRESH-TOKEN", description = "JWT Refresh Token", required = true, in = HEADER),
    })
    @Operation(summary = "JWT 토큰 재발급", description = "만료된 AccessToken과 Refresh Token으로 AccessToken을 재발급 받는다.")
    public SingleResult<ReIssueTokenResDto> reissueAccessToken(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                               @RequestHeader(value = "X-AUTH-REFRESH-TOKEN") String refreshToken) {

        return responseService.getSingleResult(userService.reissueAccessToken(token, refreshToken));
    }

    @PostMapping(value = "findId", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "아이디 찾기", description = "<strong>회원 정보<strong>로 아이디를 찾는다.")
    public CommonResult findUserIdWithUserInfo
            (@RequestBody
             @io.swagger.v3.oas.annotations.parameters.RequestBody
             @Valid FindIdReqDto request) {

        return responseService.getSuccessResult(userService.getUserIdWithUserInfo(request));
    }

    @PostMapping(value = "newpassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "비밀번호 재설정", description = "<strong>비밀번호<strong>를 재설정한다.")
    public CommonResult updatePassword
            (@RequestBody
             @io.swagger.v3.oas.annotations.parameters.RequestBody
             @Valid UserInfoReqDto request) {

        userService.updatePassword(request);

        return responseService.getSuccessResult("비밀번호가 성공적으로 재설정 되었습니다.");
    }


    @Operation(summary = "fcm 토큰 저장", description = "<strong>fcm토큰<strong> 저장한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @PostMapping(value = "fcm", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult updateFcmToken
            (@RequestBody
             @io.swagger.v3.oas.annotations.parameters.RequestBody
                     FcmTokenReqDto fcmReq,
             Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());
        User user = userService.findByUserPk(userPk);
        userService.updateFcmToken(user, fcmReq);
        return responseService.getSuccessResult("성공적으로 저장되었습니다.");
    }


    private boolean idValidate(String userId) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
        Matcher matcher = pattern.matcher(userId);

        return !(userId.isBlank() || !matcher.matches() ||
                userId.length() < USER_ID_MIN || userId.length() > USER_ID_MAX);
    }
}