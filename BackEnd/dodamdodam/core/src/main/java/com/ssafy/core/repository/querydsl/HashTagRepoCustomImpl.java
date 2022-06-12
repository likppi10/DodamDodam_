package com.ssafy.core.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.entity.HashTag;
import com.ssafy.core.entity.QAlbum;
import com.ssafy.core.entity.QHashTag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HashTagRepoCustomImpl implements HashTagRepoCustom{
    private final JPAQueryFactory jpaQueryFactory;
    QHashTag hashTag = QHashTag.hashTag;
    QAlbum album = QAlbum.album;

    @Override
    public List<HashTag> findHashTagsByAlbumId(Long albumId) {
        return jpaQueryFactory.select(hashTag)
                .from(hashTag)
                .where(hashTag.album.id.eq(albumId))
                .leftJoin(hashTag.album, album)
                .fetch();
    }

}
