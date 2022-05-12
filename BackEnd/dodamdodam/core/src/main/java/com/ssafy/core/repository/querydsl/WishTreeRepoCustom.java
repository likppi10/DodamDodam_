package com.ssafy.core.repository.querydsl;

import com.ssafy.core.entity.Family;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.entity.WishTree;

import java.util.List;

public interface WishTreeRepoCustom {
    WishTree findWishTreeByProfile(Profile profile);
    List<Long> findPositionByFamily(Family family);
}
