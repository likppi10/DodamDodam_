package com.ssafy.core.repository.querydsl;

import com.ssafy.core.entity.Family;

public interface FamilyRepoCustom {

    Family findFamilyByUserPk(Long userPk);

    Long findFamilyIdByUserPk(Long userPk);

    Long findFamilyIdByUserPK(Long userPk);
}
