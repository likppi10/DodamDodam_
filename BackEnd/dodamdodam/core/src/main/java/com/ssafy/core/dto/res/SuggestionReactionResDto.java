package com.ssafy.core.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Suggestion Reaction Response")
public class SuggestionReactionResDto {

    @Schema(description = "좋아요 여부", example = "true/false")
    private Boolean isLike;

    @Schema(description = "프로필 id", example = "1")
    private Long profileId;
}
