package com.ssafy.core.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Today Condition Response")
public class TodayConditionResDto {

    @Schema(description = "상태 이모티콘 String", example = ":smile")
    private String emotion;

    @Schema(description = "오늘의 한마디", example = "예시입니다!")
    private String comment;

}
