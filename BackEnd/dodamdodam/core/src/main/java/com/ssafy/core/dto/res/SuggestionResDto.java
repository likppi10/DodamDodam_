package com.ssafy.core.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Main Suggestion Response")
public class SuggestionResDto {

    @Schema(description = "의견 제시 Id", example = "1")
    private Long suggestionId;

    @Schema(description = "의견 내용", example = "예시입니다.")
    private String text;

    @Schema(description = "좋아요 갯수", example = "1")
    private Long likeCount;

    @Schema(description = "싫어요 갯수", example = "1")
    private Long dislikeCount;

    private List<SuggestionReactionListResDto> suggestionReactions;

}
