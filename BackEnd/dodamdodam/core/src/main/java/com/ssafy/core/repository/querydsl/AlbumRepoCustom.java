package com.ssafy.core.repository.querydsl;

import com.ssafy.core.dto.res.AlbumResDto;
import com.ssafy.core.entity.Album;

import java.time.LocalDate;
import java.util.List;

public interface AlbumRepoCustom {

    Album findAlbumByAlbumId(Long albumId);

    List<AlbumResDto> findAlbumListByFamilyId(Long familyId);

    LocalDate findAlbumDateByAlbumId(Long albumId);
}
