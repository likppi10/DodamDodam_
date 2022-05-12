package com.ssafy.api.controller;

import com.ssafy.api.service.FcmService;
import com.ssafy.api.service.ScheduleService;
import com.ssafy.core.dto.req.AlarmReqDto;
import com.ssafy.core.dto.req.CreateSuggestionReqDto;
import com.ssafy.core.dto.req.SuggestionReactionReqDto;
import com.ssafy.core.dto.res.*;
import com.ssafy.api.service.MainService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ListResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import com.ssafy.core.entity.Profile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
@Tag(name = "MainController", description = "메인컨트롤러")
public class MainController {

    private final MainService mainService;
    private final ResponseService responseService;
    private final ScheduleService scheduleService;
    private final FcmService fcmService;

    @Operation(summary = "가족 Profile 정보", description = "본인을 제외한 가족의 <strong>profile<strong> 정보를 받는다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "profileList")
    public ListResult<MainProfileResDto> getProfileList(Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());

        return responseService.getListResult(mainService.getProfileList(userPk));
    }


    //  의견 관련 API
    @Operation(summary = "의견 제시 생성", description = "<strong>의견 제시<strong>를 작성한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @PostMapping(value = "suggestion")
    public CommonResult createSuggestion(@RequestBody @Valid CreateSuggestionReqDto request, Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());

        mainService.createSuggestion(request, userPk);

        return responseService.getSuccessResult("의견 제시가 정상적으로 등록되었습니다.");
    }


    @Operation(summary = "의견 제시 삭제", description = "<strong>의견 제시<strong>를 삭제한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @DeleteMapping(value = "{suggestionId}")
    public CommonResult deleteSuggestion(@PathVariable Long suggestionId, Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());

        mainService.deleteSuggestion(suggestionId, userPk);

        return responseService.getSuccessResult("의견 제시가 정상적으로 삭제되었습니다.");
    }


    @Operation(summary = "의견 리스트 목록", description = "<strong>의견 제시 리스트<strong>를 조회한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "suggestionList")
    public ListResult<SuggestionResDto> getSuggestionList(Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());

        return responseService.getListResult(mainService.getSuggestionList(userPk));
    }


    @Operation(summary = "의견 리액션", description = "<strong>의견 제시 리액션<strong>을 등록 혹은 수정한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @PostMapping(value = "suggestion/reaction", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ListResult<SuggestionResDto> manageSuggestionReaction(
            @RequestBody @Valid SuggestionReactionReqDto request,
            Authentication authentication
    ) {

        return responseService.getListResult(
                mainService.manageSuggestionReaction(request, Long.parseLong(authentication.getName()))
        );
    }

    @Operation(summary = "오늘 일정 리스트 목록", description = "<strong>오늘 일정 리스트<strong>를 조회한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "schedule/today")
    public ListResult<ScheduleDetailResDto> getTodayScheduleList(Authentication authentication) {

        return responseService.getListResult(
                scheduleService.getScheduleListByUserPkAndDay
                        (Long.parseLong(authentication.getName()), LocalDate.now()));
    }


    @Operation(summary = "오늘의 미션 조회", description = "<strong>미션 대상<strong>과 미션 내용을 조회한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "mission")
    public SingleResult<MissionResDto> getTodayMission(Authentication authentication) {
        long userPk = Long.parseLong(authentication.getName());

        return responseService.getSingleResult(mainService.findTodayMission(userPk));
    }

    @Operation(summary = "푸쉬 알림", description = "<strong>푸쉬 알림<strong>전송 및 데이터 저장.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @PostMapping(value = "alarm")
    public CommonResult sendAlarm(@RequestBody AlarmReqDto alarmReq,
                                  Authentication authentication) throws IOException {
        Long userPk = Long.parseLong(authentication.getName());
        Profile me = mainService.getProfileByUserPk(userPk);
        Profile target = mainService.getProfileByProfilePk(alarmReq.getTargetProfileId());
        mainService.meAndTargetFamilyCheck(me, target);
        String fcmToken = mainService.getTargetFcmToken(target);
        fcmService.sendMessageTo(fcmToken, me.getNickname(), alarmReq.getContent());
        mainService.recordAlarmCount(me, alarmReq);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "알림 리스트 받기", description = "<strong>알림 리스트<strong>를 사용 횟수 순으로 받는다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "alarm/{targetProfileId}")
    public ListResult<AlarmResDto> getAlarmList(@PathVariable long targetProfileId, Authentication authentication) {
        Long userPk = Long.parseLong(authentication.getName());
        Profile me = mainService.getProfileByUserPk(userPk);
        Profile target = mainService.getProfileByProfilePk(targetProfileId);
        mainService.meAndTargetFamilyCheck(me, target);
        return responseService.getListResult(mainService.getAlarmList(me, target));
    }
}
