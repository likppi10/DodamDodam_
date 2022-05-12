package com.ssafy.core.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Model : CreateSuggestionReqDto")
public class CreateSuggestionReqDto {

    @NotBlank
    private String text;
}
