package com.ssafy.core.repository;

import com.ssafy.core.entity.SuggestionReaction;
import com.ssafy.core.repository.querydsl.SuggestionReactionRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestionReactionRepository extends JpaRepository<SuggestionReaction, Long>, SuggestionReactionRepoCustom {

}
