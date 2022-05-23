package com.ssafy.core.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ChattingMember Response")
public class ChattingMemberResDto {

    @Schema(description = "프로필 아이디", example = "1")
    private Long profileId;

    @Schema(description = "프로필 사진 경로", example = "image.png")
    private String profileImage;

    @Schema(description = "프로필 역할", example = "아들")
    private String role;
}
