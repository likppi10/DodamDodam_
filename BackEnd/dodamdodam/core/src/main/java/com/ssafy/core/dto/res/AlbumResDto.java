package com.ssafy.core.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumResDto {
    @Schema(description = "해시태그")
    private List<HashTagResDto> hashTags;

    @Schema(description = "메인사진")
    private AlbumMainResDto mainPicture;
}
