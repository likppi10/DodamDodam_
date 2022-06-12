package com.ssafy.core.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Model : FcmTokenReqDto")
public class FcmTokenReqDto {

    @NotNull
    @Schema(description = "fcm토큰", required = true, example = "c1~~")
    private String fcmToken;
}
