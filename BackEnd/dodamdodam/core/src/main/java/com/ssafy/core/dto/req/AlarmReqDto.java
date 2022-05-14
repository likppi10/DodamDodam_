package com.ssafy.core.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@AllArgsConstructor
@Schema(description = "DTO Model : AlarmReqDto")
public class AlarmReqDto {

    @NotNull
    @Schema(description = "대상 프로필 id", required = true, example = "2")
    private Long targetProfileId;

    @NotNull
    @Schema(description = "알림 내용", required = true, example = "사랑해")
    private String content;

}
