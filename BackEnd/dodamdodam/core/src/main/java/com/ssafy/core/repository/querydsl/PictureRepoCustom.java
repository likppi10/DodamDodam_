package com.ssafy.core.repository.querydsl;

import com.ssafy.core.dto.res.AlbumMainResDto;
import com.ssafy.core.dto.res.PictureResDto;
import com.ssafy.core.entity.Picture;

import java.util.List;

public interface PictureRepoCustom {

    List<Picture> findPictureListByAlbumId(Long albumId);

    Picture findPictureByPictureId(Long pictureId);

    AlbumMainResDto findMainPictureByAlbumId(Long albumId);

    List<Picture> findPictureListByPictureIdList(List<Long> deletePictureIdList);
}
