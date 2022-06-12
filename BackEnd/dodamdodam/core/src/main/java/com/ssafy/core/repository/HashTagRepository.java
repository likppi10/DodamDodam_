package com.ssafy.core.repository;

import com.ssafy.core.entity.HashTag;
import com.ssafy.core.repository.querydsl.HashTagRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<HashTag, Long>, HashTagRepoCustom {
}
