package com.ssafy.core.repository;

import com.ssafy.core.entity.Suggestion;
import com.ssafy.core.repository.querydsl.SuggestionRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long>, SuggestionRepoCustom {

    long countSuggestionByFamily_Id(long familyId);

    Optional<Suggestion> findSuggestionById(long suggestionId);

}
