package com.ssafy.core.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.dto.res.AlbumReactionResDto;
import com.ssafy.core.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlbumReactionRepoCustomImpl implements AlbumReactionRepoCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QAlbum album = QAlbum.album;
    QProfile profile = QProfile.profile;
    QAlbumReaction albumReaction = QAlbumReaction.albumReaction;

    @Override
    public AlbumReaction findReactionByAlbumIdAndProfileId(Long albumId, Long profileId) {
        return jpaQueryFactory.select(albumReaction)
                .from(albumReaction)
                .leftJoin(albumReaction.album, album)
                .fetchJoin()
                .where(albumReaction.album.id.eq(albumId).and(albumReaction.profile.id
                        .eq(profileId)))
                .fetchFirst();
    }

    @Override
    public AlbumReaction findReactionByReactionId(Long reactionId) {

        return jpaQueryFactory.select(albumReaction)
                .from(albumReaction)
                .leftJoin(albumReaction.album, album)
                .fetchJoin()
                .where(albumReaction.id.eq(reactionId))
                .fetchFirst();
    }

    @Override
    public List<AlbumReactionResDto> findAlbumReactionResDtoListByAlbumId(Long albumId) {
        return jpaQueryFactory.select(Projections.fields(
                        AlbumReactionResDto.class,
                        albumReaction.emoticon,
                        albumReaction.id.as("reactionId"),
                        profile.imagePath,
                        profile.role,
                        profile.id.as("profileId")
                ))
                .from(albumReaction)
                .join(profile)
                .on(albumReaction.profile.id.eq(profile.id))
                .where(albumReaction.album.id.eq(albumId))
                .fetch();
    }


}
