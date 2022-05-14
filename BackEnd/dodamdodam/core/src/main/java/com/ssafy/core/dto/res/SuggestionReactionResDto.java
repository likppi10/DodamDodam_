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

    @Schema(description = "의견 제시 id", example = "1")
    private Long suggestionId;

    @Schema(description = "좋아요 갯수", example = "2")
    private Long like;

    @Schema(description = "싫어요 갯수", example = "3")
    private Long dislike;
}
