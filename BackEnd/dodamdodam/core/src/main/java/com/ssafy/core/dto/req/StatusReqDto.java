package com.ssafy.core.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Model : StatusReqDto")
public class StatusReqDto {

    @NotBlank
    @Schema(description = "상태", required = true, example = "happy")
    private String emotion;

    @NotBlank
    @Size(max = 30)
    @Schema(description = "오늘의 한마디", required = true, example = "오늘 기분 최고!")
    private String comment;
}
