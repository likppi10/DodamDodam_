package com.ssafy.core.repository.querydsl;

import com.ssafy.core.dto.res.SuggestionResDto;

import java.util.List;

public interface SuggestionRepoCustom {

    List<SuggestionResDto> getSuggestionListByFamilyId(Long familyId);
}
