package com.ssafy.core.repository.querydsl;

import com.ssafy.core.entity.SuggestionReaction;

public interface SuggestionReactionRepoCustom {

    SuggestionReaction findSuggestionReactionByProfileIdAndSuggestionId(Long profileId, Long suggestionId);
}
