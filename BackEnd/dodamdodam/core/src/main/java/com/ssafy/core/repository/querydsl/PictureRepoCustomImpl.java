package com.ssafy.core.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.dto.res.AlbumMainResDto;
import com.ssafy.core.entity.*;
import com.ssafy.core.entity.Picture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PictureRepoCustomImpl implements PictureRepoCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QAlbum album = QAlbum.album;
    QPicture picture = QPicture.picture;

    @Override
    public List<Picture> findPictureListByAlbumId(Long albumId) {
        return jpaQueryFactory.selectFrom(picture)
                .where(picture.album.id.eq(albumId))
                .fetch();
    }

    @Override
    public Picture findPictureByPictureId(Long pictureId) {
        return jpaQueryFactory.select(picture)
                .from(picture)
                .where(picture.id.eq(pictureId))
                .fetchOne();
    }

    @Override
    public AlbumMainResDto findMainPictureByAlbumId(Long albumId) {
        return jpaQueryFactory.select(Projections.fields(
                        AlbumMainResDto.class,
                        picture.album.id.as("albumId"),
                        picture.album.date,
                        picture.path_name.as("imagePath")))
                .from(picture)
                .join(picture.album, album)
                .on(picture.album.id.eq(album.id))
                .where(picture.album.id.eq(albumId).and(picture.is_main.eq(true)))
                .fetchOne();

    }

    @Override
    public List<Picture> findPictureListByPictureIdList(List<Long> deletePictureIdList) {
        return jpaQueryFactory.select(picture)
                .from(picture)
                .where(picture.id.in(deletePictureIdList))
                .fetch();
    }
}
