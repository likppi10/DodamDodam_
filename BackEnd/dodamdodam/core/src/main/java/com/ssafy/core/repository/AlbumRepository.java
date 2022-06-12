package com.ssafy.core.repository;

import com.ssafy.core.entity.Album;
import com.ssafy.core.repository.querydsl.AlbumRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AlbumRepository  extends JpaRepository<Album, Long>, AlbumRepoCustom {

}
