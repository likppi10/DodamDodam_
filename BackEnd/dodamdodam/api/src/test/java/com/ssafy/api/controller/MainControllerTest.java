package com.ssafy.api.controller;

import com.ssafy.api.ControllerTestSupport;
import com.ssafy.api.WithMockCustomUser;
import com.ssafy.api.service.MainService;
import com.ssafy.api.service.ScheduleService;
import com.ssafy.core.dto.res.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MainControllerTest extends ControllerTestSupport {

    @MockBean
    private MainService mainService;

    @MockBean
    private ScheduleService scheduleService;

    final List<MainProfileResDto> expectListProfileDto = List.of(
            MainProfileResDto.builder()
                    .profileId(1L)
                    .imagePath("example.jpg")
                    .role("아빠")
                    .emotion("smile")
                    .comment("예시")
                    .build()
    );

    final SuggestionReactionListResDto expectReactionListDto = SuggestionReactionListResDto.builder()
            .isLike(true)
            .profileId(1L)
            .build();

    final SuggestionResDto expectSuggestionDto = SuggestionResDto.builder()
            .suggestionId(1L)
            .text("의견 제시 등록")
            .likeCount(1L)
            .dislikeCount(0L)
            .suggestionReactions(List.of(expectReactionListDto))
            .build();

    final ScheduleDetailResDto expectScheduleDetailDto = ScheduleDetailResDto.builder()
            .scheduleId(1L)
            .title("title")
            .content("content")
            .startDate(LocalDate.parse("2022-05-10"))
            .endDate(LocalDate.parse("2022-05-12"))
            .role("아들")
            .build();

    final MissionResDto expectMissionDto = MissionResDto.builder()
            .missionContent("엄마를 꼬옥 안아주세요!")
            .missionTarget("엄마")
            .build();

    @Test
    @WithMockCustomUser
    void getProfileList_정상작동() throws Exception {
        //given
        given(mainService.getProfileList(any())).willReturn(expectListProfileDto);

        //when
        ResultActions result = mockMvc.perform(
                get("/api/main/profileList")
                        .contentType(MediaType.APPLICATION_JSON)
        );
        MainProfileResDto expectProfileDto = expectListProfileDto.get(0);

        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.data[0].profileId").value(expectProfileDto.getProfileId()));
        result.andExpect(jsonPath("$.data[0].imagePath").value(expectProfileDto.getImagePath()));
        result.andExpect(jsonPath("$.data[0].role").value(expectProfileDto.getRole()));
        result.andExpect(jsonPath("$.data[0].comment").value(expectProfileDto.getComment()));
        result.andExpect(jsonPath("$.data[0].emotion").value(expectProfileDto.getEmotion()));
    }

    @Test
    @WithMockCustomUser
    void createSuggestion_정상작동() throws Exception {
        //given
        willDoNothing().given(mainService).createSuggestion(any(), anyLong());

        //when
        ResultActions result = mockMvc.perform(
                post("/api/main/suggestion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/main/createSuggestion/success.json"))
        );

        //then
        result.andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser
    void createSuggestion_공백입력() throws Exception {
        //given
        willDoNothing().given(mainService).createSuggestion(any(), anyLong());

        //when
        ResultActions result = mockMvc.perform(
                post("/api/main/suggestion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/main/createSuggestion/textBlank.json"))
        );

        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockCustomUser
    void deleteSuggestion_정상작동() throws Exception {
        //given
        willDoNothing().given(mainService).deleteSuggestion(anyLong(), anyLong());

        //when
        ResultActions result = mockMvc.perform(
                delete("/api/main/{suggestionId}", "1")
        );

        //then
        result.andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser
    void deleteSuggestion_ID값_문자() throws Exception {
        //given
        willDoNothing().given(mainService).deleteSuggestion(anyLong(), anyLong());

        //when
        ResultActions result = mockMvc.perform(
                delete("/api/main/{suggestionId}", "test")
        );

        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockCustomUser
    void deleteSuggestion_ID값_공백() throws Exception {
        //given
        willDoNothing().given(mainService).deleteSuggestion(anyLong(), anyLong());

        //when
        ResultActions result = mockMvc.perform(
                delete("/api/main/{suggestionId}", " ")
        );

        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockCustomUser
    void getSuggestionList_정상입력() throws Exception {
        //given
        given(mainService.getSuggestionList(anyLong())).willReturn(List.of(expectSuggestionDto));

        //when
        ResultActions result = mockMvc.perform(
                get("/api/main/suggestionList")
        );

        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.data[0].suggestionId")
                .value(expectSuggestionDto.getSuggestionId()));
        result.andExpect(jsonPath("$.data[0].text")
                .value(expectSuggestionDto.getText()));
        result.andExpect(jsonPath("$.data[0].likeCount")
                .value(expectSuggestionDto.getLikeCount()));
        result.andExpect(jsonPath("$.data[0].dislikeCount")
                .value(expectSuggestionDto.getDislikeCount()));
        result.andExpect(jsonPath("$.data[0].suggestionReactions[0].like")
                .value(expectReactionListDto.isLike()));
        result.andExpect(jsonPath("$.data[0].suggestionReactions[0].profileId")
                .value(expectReactionListDto.getProfileId()));
    }

    @Test
    @WithMockCustomUser
    void manageSuggestionReaction_정상입력() throws Exception {
        //given
        given(mainService.manageSuggestionReaction(any(),any()))
                .willReturn(List.of(expectSuggestionDto));

        //when
        ResultActions result = mockMvc.perform(
                post("/api/main/suggestion/reaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/main/manageSuggestionReaction/success.json"))
        );

        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.data[0].suggestionId")
                .value(expectSuggestionDto.getSuggestionId()));
        result.andExpect(jsonPath("$.data[0].text")
                .value(expectSuggestionDto.getText()));
        result.andExpect(jsonPath("$.data[0].likeCount")
                .value(expectSuggestionDto.getLikeCount()));
        result.andExpect(jsonPath("$.data[0].dislikeCount")
                .value(expectSuggestionDto.getDislikeCount()));
        result.andExpect(jsonPath("$.data[0].suggestionReactions[0].like")
                .value(expectReactionListDto.isLike()));
        result.andExpect(jsonPath("$.data[0].suggestionReactions[0].profileId")
                .value(expectReactionListDto.getProfileId()));
    }

    @Test
    @WithMockCustomUser
    void manageSuggestionReaction_의견ID_문자입력() throws Exception {
        //given
        given(mainService.manageSuggestionReaction(any(),any()))
                .willReturn(List.of(expectSuggestionDto));

        //when
        ResultActions result = mockMvc.perform(
                post("/api/main/suggestion/reaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/main/manageSuggestionReaction/wrongSuggestionId.json"))
        );

        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockCustomUser
    void manageSuggestionReaction_IsLike_잘못입력() throws Exception {
        //given
        given(mainService.manageSuggestionReaction(any(),any()))
                .willReturn(List.of(expectSuggestionDto));

        //when
        ResultActions result = mockMvc.perform(
                post("/api/main/suggestion/reaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/main/manageSuggestionReaction/wrongIsLike.json"))
        );

        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockCustomUser
    void getTodayScheduleList_정상입력() throws Exception {
        //given
        given(scheduleService.getScheduleListByUserPkAndDay(any(),any()))
                .willReturn(List.of(expectScheduleDetailDto));

        //when
        ResultActions result = mockMvc.perform(
                get("/api/main/schedule/today")
        );

        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.data[0].scheduleId")
                .value(expectScheduleDetailDto.getScheduleId()));
        result.andExpect(jsonPath("$.data[0].title")
                .value(expectScheduleDetailDto.getTitle()));
        result.andExpect(jsonPath("$.data[0].content")
                .value(expectScheduleDetailDto.getContent()));
        result.andExpect(jsonPath("$.data[0].startDate")
                .value(expectScheduleDetailDto.getStartDate().toString()));
        result.andExpect(jsonPath("$.data[0].endDate")
                .value(expectScheduleDetailDto.getEndDate().toString()));
        result.andExpect(jsonPath("$.data[0].role")
                .value(expectScheduleDetailDto.getRole()));

    }

    @Test
    @WithMockCustomUser
    void getTodayMission_정상입력() throws Exception {
        //given
        given(mainService.findTodayMission(anyLong()))
                .willReturn(expectMissionDto);

        //when
        ResultActions result = mockMvc.perform(
                get("/api/main/mission")
        );

        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.data.missionContent")
                .value(expectMissionDto.getMissionContent()));
        result.andExpect(jsonPath("$.data.missionTarget")
                .value(expectMissionDto.getMissionTarget()));
    }

//    @Test
//    @WithMockCustomUser
//    void sendAlarm() {
//    }
//
//    @Test
//    @WithMockCustomUser
//    void getAlarmList() {
//    }
}