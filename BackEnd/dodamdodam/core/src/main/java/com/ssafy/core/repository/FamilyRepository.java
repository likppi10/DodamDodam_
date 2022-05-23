package com.ssafy.core.repository;

import com.ssafy.core.entity.Family;
import com.ssafy.core.repository.querydsl.FamilyRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyRepository extends JpaRepository<Family, Long>, FamilyRepoCustom {

    Family findFamilyByCode(String code);

    Family findFamilyById(Long id);
}
