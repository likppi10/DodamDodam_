package com.ssafy.core.repository;


import com.ssafy.core.entity.Picture;
import com.ssafy.core.repository.querydsl.PictureRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long>, PictureRepoCustom {
}
