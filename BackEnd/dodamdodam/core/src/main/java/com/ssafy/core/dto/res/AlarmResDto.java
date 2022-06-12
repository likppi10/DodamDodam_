package com.ssafy.core.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlarmResDto {

    @Setter
    @Schema(description = "내용", example = "사랑해")
    private String content;
}
