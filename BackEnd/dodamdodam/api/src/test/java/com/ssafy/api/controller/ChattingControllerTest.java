package com.ssafy.api.controller;

import com.ssafy.api.ControllerTestSupport;
import com.ssafy.api.WithMockCustomUser;
import com.ssafy.api.service.ProfileService;
import com.ssafy.core.dto.res.ChattingMemberResDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChattingControllerTest extends ControllerTestSupport {

    @MockBean
    private ProfileService profileService;

    private final List<ChattingMemberResDto> givenListChattingDto = List.of(
            ChattingMemberResDto.builder()
                    .profileId(1L)
                    .nickname("test")
                    .profileImage("/image/test.jpg")
                    .build()
    );



    @Test
    @WithMockCustomUser(userPk = "1")
    void getChattingMemberProfile_정상동작() throws Exception {

        //given
        given(profileService.getProfileListByUserPk(anyLong()))
                .willReturn(givenListChattingDto);

        //when
        ResultActions result = mockMvc.perform(
                get("/api/chatting")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.data[0].profileId").value(1));
        result.andExpect(jsonPath("$.data[0].profileImage").value("/image/test.jpg"));
        result.andExpect(jsonPath("$.data[0].nickname").value("test"));
    }

//    @Test
//    void send_정상동작() throws Exception {
//
//    }
}