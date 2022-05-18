package com.ssafy.core.repository.querydsl;

import com.ssafy.core.dto.res.AlbumReactionListResDto;
import com.ssafy.core.entity.AlbumReaction;

import java.util.List;

public interface AlbumReactionRepoCustom {
    List<AlbumReaction> findReactionsByAlbumId(long albumId);
    AlbumReaction findReactionByAlbumId(long albumId, long profileId);

    AlbumReaction findReactionByReactionId(long reactionId, long profileId);

    List<AlbumReactionListResDto> findAlbumReactionListResDtoByAlbumId(Long albumId);
}
