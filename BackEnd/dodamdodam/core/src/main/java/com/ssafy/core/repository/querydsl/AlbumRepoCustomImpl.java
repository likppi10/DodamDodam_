package com.ssafy.core.repository.querydsl;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.dto.res.*;
import com.ssafy.core.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlbumRepoCustomImpl implements AlbumRepoCustom {
    private final JPAQueryFactory jpaQueryFactory;

    QAlbum album = QAlbum.album;
    QFamily family = QFamily.family;
    QHashTag hashTag = QHashTag.hashTag;
    QPicture picture = QPicture.picture;

    @Override
    public Album findAlbumByAlbumId(Long albumId) {

        return jpaQueryFactory.select(album)
                .from(album)
                .where(album.id.eq(albumId))
                .leftJoin(album.family, family)
                .fetchJoin()
                .fetchOne();
    }

    @Override
    @Transactional
    public List<AlbumResDto> findAlbumListByFamilyId(Long familyId) {
        return jpaQueryFactory.selectFrom(album)
                .join(hashTag)
                .on(album.id.eq(hashTag.album.id))
                .join(picture)
                .on(album.id.eq(picture.album.id))
                .where(album.family.id.eq(familyId).and(picture.is_main.eq(true)))
                .orderBy(album.id.asc())
                .transform(
                        GroupBy.groupBy(album)
                                .list(Projections.fields(
                                        AlbumResDto.class,
                                        Projections.fields(
                                                AlbumMainResDto.class,
                                                album.id.as("albumId"),
                                                album.date,
                                                picture.path_name.as("imagePath")
                                        ).as("mainPicture"),
                                        GroupBy.list(
                                                Projections.fields(
                                                        HashTagResDto.class,
                                                        hashTag.text
                                                ).skipNulls()
                                        ).as("hashTags")
                                )));
    }

    @Override
    public LocalDate findAlbumDateByAlbumId(Long albumId) {
        return jpaQueryFactory.select(album.date)
                .from(album)
                .where(album.id.eq(albumId))
                .fetchFirst();
    }


}
