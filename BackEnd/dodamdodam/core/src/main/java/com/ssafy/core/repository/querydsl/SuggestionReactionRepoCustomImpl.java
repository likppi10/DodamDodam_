package com.ssafy.core.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.entity.QSuggestionReaction;
import com.ssafy.core.entity.SuggestionReaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class SuggestionReactionRepoCustomImpl implements SuggestionReactionRepoCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QSuggestionReaction suggestionReaction = QSuggestionReaction.suggestionReaction;

    @Override
    public SuggestionReaction findSuggestionReactionByProfileIdAndSuggestionId(Long profileId, Long suggestionId) {
        return jpaQueryFactory.select(suggestionReaction)
                .from(suggestionReaction)
                .where(suggestionReaction.profile.id.eq(profileId)
                        .and(suggestionReaction.suggestion.id.eq(suggestionId)))
                .fetchFirst();
    }

}
