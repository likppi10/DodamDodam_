package com.ssafy.core.repository.querydsl;

import com.ssafy.core.dto.res.AlbumReactionResDto;
import com.ssafy.core.entity.AlbumReaction;

import java.util.List;

public interface AlbumReactionRepoCustom {
    AlbumReaction findReactionByAlbumIdAndProfileId(Long albumId, Long profileId);

    AlbumReaction findReactionByReactionId(Long reactionId);

    List<AlbumReactionResDto> findAlbumReactionResDtoListByAlbumId(Long albumId);
}
