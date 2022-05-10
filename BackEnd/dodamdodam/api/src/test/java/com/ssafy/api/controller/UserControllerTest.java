package com.ssafy.api.controller;

import com.ssafy.api.ControllerTestSupport;
import com.ssafy.api.service.UserService;
import com.ssafy.core.dto.req.FindIdReqDto;
import com.ssafy.core.dto.req.UserInfoReqDto;
import com.ssafy.core.dto.res.ReIssueTokenResDto;
import com.ssafy.core.dto.res.SignInResDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTestSupport {

    @MockBean
    private UserService userService;


    private final ReIssueTokenResDto defaultReIssueTokenResDto = ReIssueTokenResDto.builder()
            .jwtToken("dodamdodam.access.token")
            .refreshToken("dodamdodam.refresh.token")
            .build();

    private final SignInResDto defaultSignInResDto = SignInResDto.builder()
            .name("test")
            .jwtToken("accessToken")
            .refreshToken("refreshToken")
            .profileId(1L)
            .familyId(1L)
            .build();

    @Test
    void idCheck_사용가능한_아이디() throws Exception {

        //given
        willDoNothing().given(userService).checkExistId(any(String.class));

        //when
        ResultActions result = mockMvc.perform(
                get("/api/user/{userId}", "test")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk());
    }

    @Test
    void idCheck_아이디_공백포함() throws Exception {

        //given
        willDoNothing().given(userService).checkExistId(any(String.class));

        //when
        ResultActions result = mockMvc.perform(
                get("/api/user/{userId}", "tes t")
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void idCheck_아이디_특수문자_포함() throws Exception {

        //given
        willDoNothing().given(userService).checkExistId(any(String.class));

        //when
        ResultActions result = mockMvc.perform(
                get("/api/user/{userId}", "test!@#$")
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void userSignUp_정상입력() throws Exception {
        //given, when
        ResultActions result = mockMvc.perform(
                post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/signUp/success.json"))
        );

        //then
        result.andExpect(status().isOk());
    }

    @Test
    void userSignUp_한글_아이디_사용() throws Exception {
        //given, when
        ResultActions result = mockMvc.perform(
                post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/signUp/wrongId.json"))
        );
        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void userSignUp_비밀번호_특수문자_입력안함() throws Exception {
        //given, when
        ResultActions result = mockMvc.perform(
                post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/signUp/wrongPassword.json"))
        );

        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void userSignUp_이름_입력안함() throws Exception {
        //given, when
        ResultActions result = mockMvc.perform(
                post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/signUp/wrongName.json"))
        );

        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void userSignIn_정상입력() throws Exception {
        //given
        given(userService.signIn(any(UserInfoReqDto.class))).willReturn(defaultSignInResDto);

        //when
        ResultActions result = mockMvc.perform(
                post("/api/user/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/signIn/success.json"))
        );
        //then
        result.andExpect(status().isOk());
    }

    @Test
    void userSignIn_비밀번호_형식_틀림() throws Exception {
        //given
        given(userService.signIn(any(UserInfoReqDto.class))).willReturn(defaultSignInResDto);

        //when
        ResultActions result = mockMvc.perform(
                post("/api/user/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/signIn/wrongPassword.json"))
        );
        //then
        result.andExpect(status().is4xxClientError());
    }


    @Test
    void userSignIn_아이디_공백() throws Exception {
        //given
        given(userService.signIn(any(UserInfoReqDto.class))).willReturn(defaultSignInResDto);

        //when
        ResultActions result = mockMvc.perform(
                post("/api/user/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/signIn/blankId.json"))
        );
        //then
        result.andExpect(status().is4xxClientError());
    }


    @Test
    @WithMockUser
    void reissueAccessToken_정상입력() throws Exception {
        //given
        given(userService.reissueAccessToken(any(String.class),any(String.class)))
                .willReturn(defaultReIssueTokenResDto);

        //when
        ResultActions result = mockMvc.perform(
                post("/api/user/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-AUTH-TOKEN", "token")
                        .header("X-AUTH-REFRESH-TOKEN", "refreshToken")
        );
        //then
        result.andExpect(status().isOk());
    }


    @Test
    void findUserIdWithUserInfo_정상입력() throws Exception {
        //given
        given(userService.findUserIdWithUserInfo(any(FindIdReqDto.class)))
                .willReturn("test");

        //when
        ResultActions result = mockMvc.perform(
                post("/api/user/findId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/findId/success.json"))
        );
        //then
        result.andExpect(status().isOk());
    }

    @Test
    void findUserIdWithUserInfo_날짜_형식_틀림() throws Exception {
        //given
        given(userService.findUserIdWithUserInfo(any(FindIdReqDto.class)))
                .willReturn("test");

        //when
        ResultActions result = mockMvc.perform(
                post("/api/user/findId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/findId/wrongDate.json"))
        );
        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void findUserIdWithUserInfo_가족코드_형식_틀림() throws Exception {
        //given
        given(userService.findUserIdWithUserInfo(any(FindIdReqDto.class)))
                .willReturn("test");

        //when
        ResultActions result = mockMvc.perform(
                post("/api/user/findId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/findId/wrongFamilyCode.json"))
        );
        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void updatePassword_정상입력() throws Exception {
        //given
        willDoNothing().given(userService).updatePassword(any(UserInfoReqDto.class));

        //when
        ResultActions result = mockMvc.perform(
                post("/api/user/newpassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/updatePassword/success.json"))
        );
        //then
        result.andExpect(status().isOk());
    }

    @Test
    void updatePassword_아이디_공백() throws Exception {
        //given
        willDoNothing().given(userService).updatePassword(any(UserInfoReqDto.class));

        //when
        ResultActions result = mockMvc.perform(
                post("/api/user/newpassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/updatePassword/blankId.json"))
        );
        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void updatePassword_비밀번호_형식_틀림() throws Exception {
        //given
        willDoNothing().given(userService).updatePassword(any(UserInfoReqDto.class));

        //when
        ResultActions result = mockMvc.perform(
                post("/api/user/newpassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/updatePassword/wrongPassword.json"))
        );
        //then
        result.andExpect(status().is4xxClientError());
    }
//    @Test
//    void updateFcmToken() throws Exception {
//    }
}