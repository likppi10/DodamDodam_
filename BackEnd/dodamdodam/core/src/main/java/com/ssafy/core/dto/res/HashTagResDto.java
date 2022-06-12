package com.ssafy.core.dto.res;

import com.ssafy.core.entity.HashTag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HashTagResDto {
    @Schema(description = "해시태그", example = "#여름")
    private String text;

    public static HashTagResDto of(HashTag hashTag) {
        return HashTagResDto.builder()
                .text(hashTag.getText())
                .build();
    }
}
