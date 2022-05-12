package com.ssafy.core.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.dto.res.WishTreeResDto;
import com.ssafy.core.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WishTreeRepoCustomImpl implements WishTreeRepoCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QWishTree wishTree = QWishTree.wishTree;
    QProfile profile = QProfile.profile;

    @Override
    public WishTree findWishTreeByProfile(Profile profile) {
        return jpaQueryFactory.selectFrom(wishTree)
                .where(wishTree.profile.eq(profile))
                .fetchFirst();
    }

    @Override
    public List<Long> findPositionByFamily(Family family) {
        return jpaQueryFactory.select(wishTree.position)
                .from(wishTree)
                .where(wishTree.family.eq(family))
                .fetch();
    }

    @Override
    public List<WishTreeResDto.WishTreeDetail> findWishTreeListByFamily(Family family) {
        System.out.println("3");
        return jpaQueryFactory.select(Projections.fields(WishTreeResDto.WishTreeDetail.class,
                wishTree.id.as("wishTreeId"),
                wishTree.content,
                wishTree.position,
                wishTree.profile.role,
                wishTree.profile.imagePath.as("profileImage")
                ))
                .from(wishTree)
                .where(wishTree.family.eq(family))
                .fetch();
    }
}
