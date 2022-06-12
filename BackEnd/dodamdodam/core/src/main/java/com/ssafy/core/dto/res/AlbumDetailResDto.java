package com.ssafy.core.dto.res;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDetailResDto {

    @NotNull
    @Schema(description = "날짜", example = "2020-07-01")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate date;

    @NotNull
    @Schema(description = "사진")
    private List<PictureResDto> pictures;

    @NotNull
    @Schema(description = "해시태그")
    private List<HashTagResDto> hashTags;

    @Nullable
    @Schema(description = "앨범 리액션")
    private List<AlbumReactionResDto> albumReactions;

}
