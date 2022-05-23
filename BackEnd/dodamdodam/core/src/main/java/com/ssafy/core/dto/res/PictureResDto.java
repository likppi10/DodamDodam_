package com.ssafy.core.dto.res;

import com.ssafy.core.entity.Picture;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PictureResDto {

    @Schema(description = "메인여부", example = "true")
    private Boolean isMain;

    @Schema(description = "사진경로", example = "image/picture.jpg")
    private String imagePath;

    @Schema(description = "사진 Id", example = "1")
    private Long pictureId;

    public static PictureResDto of(Picture picture) {
        return PictureResDto.builder()
                .isMain(picture.is_main())
                .imagePath(picture.getPath_name())
                .pictureId(picture.getId())
                .build();
    }
}
