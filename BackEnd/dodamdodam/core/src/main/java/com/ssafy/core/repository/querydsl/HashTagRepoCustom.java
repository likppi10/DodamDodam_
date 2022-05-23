package com.ssafy.core.repository.querydsl;

import com.ssafy.core.entity.HashTag;
import java.util.List;

public interface HashTagRepoCustom {
    List<HashTag> findHashTagsByAlbumId(Long albumId);

}
