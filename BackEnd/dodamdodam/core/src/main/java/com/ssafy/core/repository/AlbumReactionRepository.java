package com.ssafy.core.repository;

import com.ssafy.core.entity.AlbumReaction;
import com.ssafy.core.repository.querydsl.AlbumReactionRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumReactionRepository extends JpaRepository<AlbumReaction, Long>, AlbumReactionRepoCustom {
}
