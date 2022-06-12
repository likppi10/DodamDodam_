package com.ssafy.core.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Model : MissionResDto")
public class MissionResDto {

    @Schema(description = "미션 내용", example = "따뜻하게 안아주세요!")
    private String missionContent;

    @Schema(description = "미션 대상", example = "엄마")
    private String missionTarget;
}
