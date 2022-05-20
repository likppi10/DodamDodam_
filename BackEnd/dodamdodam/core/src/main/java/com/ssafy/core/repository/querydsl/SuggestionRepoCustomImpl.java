package com.ssafy.core.repository.querydsl;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.dto.res.SuggestionReactionListResDto;
import com.ssafy.core.dto.res.SuggestionResDto;
import com.ssafy.core.entity.QSuggestion;
import com.ssafy.core.entity.QSuggestionReaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SuggestionRepoCustomImpl implements SuggestionRepoCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QSuggestion suggestion = QSuggestion.suggestion;
    QSuggestionReaction suggestionReaction = QSuggestionReaction.suggestionReaction;

    @Override
    @Transactional
    public List<SuggestionResDto> getSuggestionListByFamilyId(Long familyId) {
        return jpaQueryFactory.selectFrom(suggestion)
                .leftJoin(suggestionReaction)
                .on(suggestion.id.eq(suggestionReaction.suggestion.id))
                .where(suggestion.family.id.eq(familyId))
                .orderBy(suggestion.id.asc())
                .transform(
                        GroupBy.groupBy(suggestion)
                                .list(Projections.fields(
                                        SuggestionResDto.class,
                                        suggestion.id.as("suggestionId"),
                                        suggestion.text,
                                        suggestion.likeCount,
                                        suggestion.dislikeCount,
                                        GroupBy.list(
                                                Projections.fields(
                                                        SuggestionReactionListResDto.class,
                                                        suggestionReaction.profile.id.as("profileId"),
                                                        suggestionReaction.isLike
                                                ).skipNulls()
                                        ).as("suggestionReactions")
                                )));
    }
}