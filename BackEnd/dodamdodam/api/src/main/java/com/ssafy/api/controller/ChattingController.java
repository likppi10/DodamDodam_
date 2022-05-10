package com.ssafy.api.controller;

import com.ssafy.api.service.FcmService;
import com.ssafy.api.service.MainService;
import com.ssafy.api.service.ProfileService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ListResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.core.dto.res.ChattingMemberResDto;
import com.ssafy.core.dto.res.MainProfileResDto;
import com.ssafy.core.entity.Profile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatting")
@Tag(name = "ChattingController", description = "채팅 컨트롤러")
public class ChattingController {

    private final ProfileService profileService;
    private final ResponseService responseService;
    private final MainService mainService;
    private final FcmService fcmService;
    @Operation(summary = "채팅 맴버들 프로필 조회", description = "<strong>채팅 맴버들<strong>의 프로필을 조회한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "")
    public ListResult<ChattingMemberResDto> getChattingMemberProfile(Authentication authentication) {
        long userPk = Long.parseLong(authentication.getName());

        return responseService.getListResult(profileService.getProfileListByUserPk(userPk));
    }

    @Operation(summary = "채팅 맴버들 프로필 조회", description = "<strong>채팅 맴버들<strong>의 프로필을 조회한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @PostMapping(value = "/send")
    public CommonResult send(@RequestParam String text, Authentication authentication) throws  IOException {
        Long userPk = Long.parseLong(authentication.getName());
        Profile me = mainService.getProfileByUserPk(userPk);
        List<MainProfileResDto> familyList = mainService.getProfileList(userPk);
        for (MainProfileResDto pf : familyList) {
            if (me.getId() != pf.getProfileId()) {
                Profile target = mainService.getProfileByProfilePk(pf.getProfileId());
                fcmService.sendMessageTo(mainService.getTargetFcmToken(target), "채팅 알림", text);
            }
        }
        return responseService.getSuccessResult();
    }

}
