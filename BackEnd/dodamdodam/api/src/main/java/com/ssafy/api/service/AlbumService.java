package com.ssafy.api.service;


import com.ssafy.core.dto.req.AlbumReactionReqDto;
import com.ssafy.core.dto.req.AlbumReqDto;
import com.ssafy.core.dto.req.AlbumUpdateReqDto;
import com.ssafy.core.dto.res.*;
import com.ssafy.core.entity.*;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ssafy.core.common.FileUtil.FILE_MAX_SIZE;
import static com.ssafy.core.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final FamilyRepository familyRepository;
    private final HashTagRepository hashTagRepository;
    private final PictureRepository pictureRepository;
    private final AlbumReactionRepository albumReactionRepository;
    private final ProfileRepository profileRepository;
    private final FileService fileService;

    @Transactional(readOnly = true)
    public Album getAlbumByAlbumId(Long albumId) {

        Album album = albumRepository.findAlbumByAlbumId(albumId);
        if (album == null) {
            throw new CustomException(ALBUM_DOES_NOT_EXIST);
        }
        return album;
    }

    @Transactional(readOnly = true)
    public List<Picture> getPictureListByAlbumId(Long albumId) {

        List<Picture> pictureList = pictureRepository.findPictureListByAlbumId(albumId);
        if (pictureList == null) {
            throw new CustomException(PICTURE_DOES_NOT_EXIST);
        }
        return pictureList;
    }

    @Transactional(readOnly = true)
    public List<HashTag> getHashTagListByAlbumId(Long albumId) {

        List<HashTag> hashTagList = hashTagRepository.findHashTagsByAlbumId(albumId);
        if (hashTagList == null) {
            throw new CustomException(HASHTAG_DOES_NOT_EXIST);
        }
        return hashTagList;
    }

    @Nullable
    @Transactional(readOnly = true)
    public List<AlbumReactionResDto> findAlbumReactionResDtoListByAlbumId(Long albumId) {

        return albumReactionRepository.findAlbumReactionResDtoListByAlbumId(albumId);
    }

    @Transactional
    public void createHashtag(String hashTag, Album album) {
        hashTagRepository.save(HashTag.builder()
                .album(album)
                .text(hashTag)
                .build());
    }

    @Transactional
    public void updateHashTag(List<HashTag> hashTagList, List<String> updateHashTagList, Album album) {

        //해시태그가 기존보다 추가 된 경우
        if (updateHashTagList.size() >= hashTagList.size()) {
            for (int i = 0; i < updateHashTagList.size(); i++) {
                if (i < hashTagList.size()) {
                    hashTagList.get(i).updateText(updateHashTagList.get(i));
                    hashTagRepository.save(hashTagList.get(i));
                } else {
                    HashTag hashTag = HashTag.builder()
                            .album(album)
                            .text(updateHashTagList.get(i))
                            .build();
                    hashTagRepository.save(hashTag);
                }
            }
        }
        //해시태그가 기존보다 줄어든 경우
        else {
            for (int i = 0; i < hashTagList.size(); i++) {
                if (i < updateHashTagList.size()) {
                    hashTagList.get(i).updateText(updateHashTagList.get(i));
                    hashTagRepository.save(hashTagList.get(i));
                } else {
                    hashTagRepository.delete(hashTagList.get(i));
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public AlbumReaction getReactionByReactionId(Long reactionId) {
        AlbumReaction albumReaction = albumReactionRepository.findReactionByReactionId(reactionId);
        if (albumReaction == null) {
            throw new CustomException(REACTION_DOES_NOT_EXIST);
        }
        return albumReaction;
    }


    /**
     * Album Service for Album Controller
     */

    @Nullable
    @Transactional(readOnly = true)
    public List<AlbumResDto> findAlbumList(Long userPk) {

        Long familyId = familyRepository.findFamilyIdByUserPk(userPk);
        return albumRepository.findAlbumListByFamilyId(familyId);
    }

    @Transactional(readOnly = true)
    public AlbumDetailResDto getAlbumDetail(Long albumId) {
        LocalDate date = albumRepository.findAlbumDateByAlbumId(albumId);
        if (date == null) {
            throw new CustomException(ALBUM_DOES_NOT_EXIST);
        }
        //1. 사진 가져오기
        List<Picture> pictureList = this.getPictureListByAlbumId(albumId);
        List<PictureResDto> pictureResDtoList = pictureList
                .stream()
                .map(PictureResDto::of)
                .collect(Collectors.toList());

        //2. 해시태그 가져오기
        List<HashTag> hashTagList = this.getHashTagListByAlbumId(albumId);
        List<HashTagResDto> hashTagResDtoList = hashTagList
                .stream()
                .map(HashTagResDto::of)
                .collect(Collectors.toList());

        //3. 리엑션 가져오기
        List<AlbumReactionResDto> albumReactionResDtoList =
                this.findAlbumReactionResDtoListByAlbumId(albumId);

        return AlbumDetailResDto.builder()
                .date(date)
                .pictures(pictureResDtoList)
                .hashTags(hashTagResDtoList)
                .albumReactions(albumReactionResDtoList)
                .build();
    }

    @Transactional
    public void createAlbum(AlbumReqDto albumReqDto, Long userPk) {

        Family family = familyRepository.findFamilyByUserPk(userPk);

        //1. 앨범 생성
        Album album = albumRepository.save(Album.builder()
                .family(family)
                .date(albumReqDto.getDate())
                .build());

        //2. 해시태그 중복 검사
        List<String> hashTagList = albumReqDto.getHashTags()
                .stream().
                distinct()
                .collect(Collectors.toList());

        //3. 해시태그 저장
        for (String hashTag : hashTagList) {
            this.createHashtag(hashTag, album);
        }

        String filePath;
        String originName;
        Long mainIndex = albumReqDto.getMainIndex();
        Long cnt = 0L;

        //4. 사진 저장
        List<MultipartFile> fileList = albumReqDto.getMultipartFiles();
        for (MultipartFile file : fileList) {
            originName = file.getOriginalFilename();
            filePath = fileService.uploadFileV1("album", file);

            boolean isMain = mainIndex.equals(cnt);

            Picture picture = pictureRepository.save(Picture.builder()
                    .album(album)
                    .origin_name(originName)
                    .path_name(filePath)
                    .is_main(isMain)
                    .build()
            );

            if (file.getSize() > FILE_MAX_SIZE) {
                fileService.resizeImage(file, picture);
            }
            cnt++;
        }
    }

    @Transactional
    public void manageAlbumReaction(AlbumReactionReqDto albumReactionReqDto, Long userPk) {
        Profile profile = profileRepository.findProfileByUserPk(userPk);
        Album album = this.getAlbumByAlbumId(albumReactionReqDto.getAlbumId());

        AlbumReaction albumReaction =
                albumReactionRepository.findReactionByAlbumIdAndProfileId(
                        album.getId(), profile.getId()
                );

        if (albumReaction == null) {
            albumReaction = AlbumReaction.builder()
                    .profile(profile)
                    .album(album)
                    .emoticon(albumReactionReqDto.getEmoticon())
                    .build();
            albumReactionRepository.save(albumReaction);
        } else {
            albumReaction.updateEmoticon(albumReactionReqDto.getEmoticon());
            albumReactionRepository.save(albumReaction);
        }
    }

    @Transactional
    public void deleteAlbumReaction(Long userPk, Long reactionId) {

        Long profileId = profileRepository.findProfileIdByUserPk(userPk);

        AlbumReaction albumReaction = this.getReactionByReactionId(reactionId);

        if (albumReaction.getProfile().getId() != profileId) {
            throw new CustomException(NO_PERMISSION);
        }
        albumReactionRepository.delete(albumReaction);
    }

    @Transactional
    public void updateAlbum(AlbumUpdateReqDto albumUpdateReqDto) {
        System.out.println("updateAlbum Start ===========");
        //1. 앨범 날짜 업데이트
        Album album = getAlbumByAlbumId(albumUpdateReqDto.getAlbumId());
        album.updateAlbumDate(albumUpdateReqDto.getDate());
        album = albumRepository.save(album);

        System.out.println("done update date ===========");
        //2. 해시태그 업데이트
        List<HashTag> hashTagList = hashTagRepository.findHashTagsByAlbumId(album.getId());
        List<String> updateHashTagList = albumUpdateReqDto.getHashTags()
                .stream()
                .distinct()
                .collect(Collectors.toList());

        this.updateHashTag(hashTagList, updateHashTagList, album);
        System.out.println("done hashtag update ===========");

        //3. 삭제할 사진 삭제
        Integer[] deletePictureListIndex = albumUpdateReqDto.getPictureIdList();
        if (deletePictureListIndex != null) {
            List<Picture> pictureList = pictureRepository.findPictureListByAlbumId(album.getId());
            List<Long> deletePictureIdList = new ArrayList<>();
            for(int id : deletePictureListIndex){
                deletePictureIdList.add(pictureList.get(id).getId());
            }

            List<Picture> deletePictureList =
                    pictureRepository.findPictureListByPictureIdList(deletePictureIdList);

            pictureRepository.deleteAll(deletePictureList);
        }

        System.out.println("done delete pictures ===========");
        //4. 새로운 사진 저장
        List<MultipartFile> fileList = albumUpdateReqDto.getMultipartFiles();

        if (fileList != null) {
            for (MultipartFile file : fileList) {
                String originFileName = file.getOriginalFilename();
                String filePath = fileService.uploadFileV1("album", file);

                Picture picture = Picture.builder()
                        .album(album)
                        .origin_name(originFileName)
                        .path_name(filePath)
                        .build();
                picture = pictureRepository.save(picture);

                if (file.getSize() > FILE_MAX_SIZE) {
                    fileService.resizeImage(file, picture);
                }
            }
        }

        System.out.println("done new picture save ===========");
        //5. 메인 사진 변경
        List<Picture> pictureList = this.getPictureListByAlbumId(album.getId());
        int idx = 0;
        for (Picture picture : pictureList) {
            picture.updateIsMain(idx == albumUpdateReqDto.getMainIndex());
            idx++;
        }
        System.out.println("done main picutre update ===========");
        pictureRepository.saveAll(pictureList);
    }

    @Transactional
    public void deleteAlbum(Long albumId, Long userPK) {

        Long familyId = familyRepository.findFamilyIdByUserPk(userPK);
        Album album = this.getAlbumByAlbumId(albumId);

        if (album.getFamily().getId() != familyId) {
            throw new CustomException(NO_PERMISSION);
        }
        albumRepository.delete(album);
    }
}
