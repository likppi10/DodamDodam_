package com.ssafy.core.repository.querydsl;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.dto.res.AlbumReactionListResDto;
import com.ssafy.core.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlbumReactionRepoCustomImpl implements AlbumReactionRepoCustom{

    private final JPAQueryFactory jpaQueryFactory;

    QAlbum album = QAlbum.album;
    QProfile profile = QProfile.profile;
    QAlbumReaction albumReaction = QAlbumReaction.albumReaction;

    @Override
    public List<AlbumReaction> findReactionsByAlbumId(long albumId) {

        return jpaQueryFactory.select(albumReaction)
                .from(albumReaction)
                .leftJoin(albumReaction.album, album)
                .fetchJoin()
                .where(albumReaction.album.id.eq(albumId))
                .fetch();
    }

    @Override
    public AlbumReaction findReactionByAlbumId(long albumId, long profileId) {
        return jpaQueryFactory.select(albumReaction)
                .from(albumReaction)
                .leftJoin(albumReaction.album, album)
                .fetchJoin()
                .where(albumReaction.album.id.eq(albumId).and(albumReaction.profile.id
                        .eq(profileId)))
                .fetchFirst();
    }

    @Override
    public AlbumReaction findReactionByReactionId(long reactionId, long profileId) {

        return jpaQueryFactory.select(albumReaction)
                .from(albumReaction)
                .leftJoin(albumReaction.album, album)
                .fetchJoin()
                .where(albumReaction.id.eq(reactionId).and(albumReaction.profile.id.eq(profileId)))
                .fetchFirst();
    }

    @Override
    public List<AlbumReactionListResDto> findAlbumReactionListResDtoByAlbumId(Long albumId) {
        return jpaQueryFactory.select(Projections.fields(AlbumReactionListResDto.class,
                albumReaction.emoticon,
                albumReaction.id.as("reactionId"),
                profile.id.as("profileId"),
                profile.role,
                profile.imagePath))
                .from(albumReaction)
                .leftJoin(profile)
                .on(albumReaction.profile.id.eq(profile.id))
                .fetch();
    }


}
