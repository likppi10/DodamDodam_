package com.ssafy.core.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumReactionResDto {

    @Schema(description = "이모티콘", example = "emoticon/smile.jpg")
    private String emoticon;

    @Schema(description = "리액션id", example = "1")
    private Long reactionId;

    @Schema(description = "프로필 사진", example = "profile/profile.jpg.")
    private String imagePath;

    @Schema(description = "역할", example = "첫째 아들")
    private String role;

    @Schema(description = "프로필id", example = "1")
    private Long profileId;

}
