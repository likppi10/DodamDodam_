package com.ssafy.core.repository;

import com.ssafy.core.entity.WishTree;
import com.ssafy.core.repository.querydsl.WishTreeRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishTreeRepository extends JpaRepository<WishTree, Long>, WishTreeRepoCustom {
}
