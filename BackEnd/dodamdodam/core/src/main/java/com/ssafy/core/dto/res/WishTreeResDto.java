package com.ssafy.core.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "WishTree Response")
public class WishTreeResDto {

    @Schema(description = "위시 트리 생성 여부", example = "true")
    private Long myWishPosition;

    private List<WishTreeDetail> wishTree;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class WishTreeDetail {
        @Schema(description = "id", example = "1")
        private Long wishTreeId;
        @Schema(description = "항목", example = "엄마손파이")
        private String content;
        @Schema(description = "자리", example = "0")
        private Long position;
        @Schema(description = "역할", example = "아들")
        private String role;
        @Schema(description = "프로필 이미지", example = "사진 경로")
        private String profileImage;
    }

}
