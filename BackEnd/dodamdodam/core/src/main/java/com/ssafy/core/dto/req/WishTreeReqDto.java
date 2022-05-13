package com.ssafy.core.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Model : WishListReqDto")
public class WishTreeReqDto {

    @Schema(description = "위시 리스트 항목", required = true, example = "엄마손파이")
    private String content;
}
