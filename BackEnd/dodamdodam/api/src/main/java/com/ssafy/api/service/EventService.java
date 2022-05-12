package com.ssafy.api.service;

import com.ssafy.core.dto.req.WishTreeReqDto;
import com.ssafy.core.dto.res.WishTreeResDto;
import com.ssafy.core.entity.Family;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.entity.User;
import com.ssafy.core.entity.WishTree;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.exception.ErrorCode;
import com.ssafy.core.repository.UserRepository;
import com.ssafy.core.repository.WishTreeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {
    private final WishTreeRepository wishTreeRepository;
    public void createWishTree(Profile profile, Family family, WishTreeReqDto wishTreeReq) {
        if (wishTreeRepository.findWishTreeByProfile(profile) != null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "위시 트리는 하나만 생성 가능합니다.");
        }
        List<Long> usedPosition = wishTreeRepository.findPositionByFamily(family);
        long[] arr = {0,1,2,3,4,5,6,7};
        Long[] longObjects = ArrayUtils.toObject(arr);
        List<Long> allPosition = new ArrayList<>(Arrays.asList(longObjects));
        for (Long position : usedPosition) {
            allPosition.remove(position);
        }
        if (allPosition.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "위시 트리가 가득 찼습니다.");
        }
        Random rand = new Random();

        wishTreeRepository.save(WishTree.builder()
                .profile(profile)
                .family(family)
                .content(wishTreeReq.getContent())
                .position(allPosition.get(rand.nextInt(allPosition.size())))
                .build());
    }

    public WishTreeResDto getWishTree(Profile profile, Family family) {
        List<WishTreeResDto.WishTreeDetail> wishTreeDetailList = wishTreeRepository.findWishTreeListByFamily(family);
        boolean canCreate;
        canCreate = wishTreeRepository.findWishTreeByProfile(profile) == null;
        return WishTreeResDto.builder()
                .wishTree(wishTreeDetailList)
                .canCreate(canCreate)
                .build();
    }

    public void updateWishTree(Profile profile, WishTreeReqDto wishListReq, long wishTreeId) {
        WishTree wishTree = wishTreeRepository.findWishTreeById(wishTreeId);
        if (wishTree.getProfile().getId() != profile.getId()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "해당 권한이 없습니다.");
        }
        wishTree.setContent(wishListReq.getContent());
        wishTreeRepository.save(wishTree);
    }
}
