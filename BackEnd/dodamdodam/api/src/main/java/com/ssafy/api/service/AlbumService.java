package com.ssafy.api.service;


import com.ssafy.core.dto.req.AlbumReactionReqDto;
import com.ssafy.core.dto.req.AlbumReqDto;
import com.ssafy.core.dto.req.AlbumUpdateReqDto;
import com.ssafy.core.dto.res.AlbumMainResDto;
import com.ssafy.core.dto.res.AlbumReactionListResDto;
import com.ssafy.core.entity.*;
import com.ssafy.core.exception.ErrorCode;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

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
    public List<Album> findAlbumsByFamilyId(Long familyId) {

        return albumRepository.findAlbumByFamilyId(familyId);
    }


    @Transactional(readOnly = true)
    public Album findByAlbum(Long albumId) {

        return albumRepository.findAlbumByAlbumId(albumId);
    }

    @Transactional(readOnly = true)
    public List<Picture> findPicturesByAlbumId(Long albumId) {

        return pictureRepository.findPicturesByAlbumId(albumId);
    }

    @Transactional(readOnly = true)
    public List<AlbumReaction> findAlbumReactionsByAlbumId(Long albumId) {

        return albumReactionRepository.findReactionsByAlbumId(albumId);
    }

    @Transactional(readOnly = true)
    public List<HashTag> findHashTagsByAlbumId(Long albumId) {

        return hashTagRepository.findHashTagsByAlbumId(albumId);
    }

    @Transactional(readOnly = true)
    public Family findFamilyByUserPK(Long userPK) {

        Family family = familyRepository.findFamilyByUserPk(userPK);
        if (family == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        return family;
    }


    @Transactional(readOnly = true)
    public AlbumMainResDto findMainPictureByAlbumId(long albumId) {

        return pictureRepository.findMainPictureByAlbumId(albumId);
    }

    @Transactional
    public void deleteAlbum(long albumId, long userPK) {
        long familyId = familyRepository.findFamilyIdByUserPk(userPK);
        Album album = albumRepository.findAlbumByAlbumId(albumId);
        if (album.getFamily().getId() != familyId) {
            throw new CustomException(ErrorCode.NOT_BELONG_FAMILY);
        }
        albumRepository.delete(album);

    }


    @Transactional
    public void createAlbum(AlbumReqDto albumReqDto, Family family, Album album, List<MultipartFile> multipartFiles) {

        String familyCode = family.getCode();

        List<String> hashTags = albumReqDto.getHashTags();

        String[] originFileNames = new String[multipartFiles.size()];

        String saveFileName = "";
        String filePath = "";
        boolean isMain = false;
        for (int i = 0; i < multipartFiles.size(); i++) {
            originFileNames[i] = multipartFiles.get(i).getOriginalFilename();

            if (albumReqDto.getMainIndex() == i) isMain = true;
            else isMain = false;
            filePath = fileService.uploadFileV1("album", multipartFiles.get(i));
            Picture picture = Picture.builder()
                    .album(album)
                    .origin_name(multipartFiles.get(i).getOriginalFilename())
                    .path_name(filePath)
                    .is_main(isMain)
                    .build();
            pictureRepository.save(picture);
            fileService.resizeImage("album", multipartFiles.get(i), picture);
        }

        for (int i = 0; i < hashTags.size(); i++) {
            HashTag hashTag = HashTag.builder()
                    .album(album)
                    .text(hashTags.get(i))
                    .build();
            hashTagRepository.save(hashTag);

        }

        albumRepository.save(album);
    }

    @Transactional(readOnly = false)
    public void updateAlbum(Long userPk, Album album, AlbumUpdateReqDto albumUpdateReqDto) {
        Family family = findFamilyByUserPK(userPk);
        album.updateLocalDate(albumUpdateReqDto.getDate());
        albumRepository.save(album);
        boolean flag;
        List<String> updateHashTags = albumUpdateReqDto.getHashTags();
        List<HashTag> hashTags = hashTagRepository.findHashTagsByAlbumId(album.getId());
        List<Picture> pictures = pictureRepository.findPicturesByAlbumId(album.getId());
        List<MultipartFile> multipartFiles = new ArrayList<>();
        if (albumUpdateReqDto.getMultipartFiles() == null) {
            flag = false;
        } else {
            multipartFiles = albumUpdateReqDto.getMultipartFiles();
            flag = true;

        }
        int[] deleteIdList = albumUpdateReqDto.getPictureIdList();

        //해시태그가 기존보다 추가 된 경우
        if (updateHashTags.size() >= hashTags.size()) {
            for (int i = 0; i < updateHashTags.size(); i++) {
                if (i < hashTags.size()) {
                    hashTags.get(i).updateText(updateHashTags.get(i));
                    hashTagRepository.save(hashTags.get(i));
                } else {
                    HashTag hashTag = HashTag.builder()
                            .album(album)
                            .text(updateHashTags.get(i))
                            .build();
                    hashTagRepository.save(hashTag);
                }
            }
        }
        //해시태그가 기존보다 줄어든 경우
        else {
            for (int i = 0; i < hashTags.size(); i++) {
                if (i < updateHashTags.size()) {
                    hashTags.get(i).updateText(updateHashTags.get(i));
                    hashTagRepository.save(hashTags.get(i));
                } else {
                    hashTagRepository.delete(hashTags.get(i));
                }
            }
        }
        int mainIndex = albumUpdateReqDto.getMainIndex();
        boolean isMain = false;
        //삭제할 사진이 있는 경우
        if (deleteIdList != null) {
            for (int i = 0; i < deleteIdList.length; i++) {
                Picture picture = pictureRepository.findPictureByPictureId(deleteIdList[i]);
                pictureRepository.delete(picture);
            }
            //추가된 사진 저장
            if (flag) {
                for (int j = 0; j < multipartFiles.size(); j++) {
                    String originFileName = multipartFiles.get(j).getOriginalFilename();
                    String filePath = fileService.uploadFileV1("album", multipartFiles.get(j));

                    Picture picture = Picture.builder()
                            .album(album)
                            .origin_name(originFileName)
                            .path_name(filePath)
                            .build();
                    pictureRepository.save(picture);
                    fileService.resizeImage("album", multipartFiles.get(j), picture);
                }
            }
            //메인 사진 업데이트
            List<Picture> newPictureList = pictureRepository.findPicturesByAlbumId(album.getId());
            for (int k = 0; k < newPictureList.size(); k++) {
                if (mainIndex == k) {
                    newPictureList.get(k).updateIsMain(true);
                } else {
                    newPictureList.get(k).updateIsMain(false);
                }

            }
        }
        //삭제할 사진이 없는 경우
        else {
            //추가된 사진 저장
            if (flag) {
                for (int j = 0; j < multipartFiles.size(); j++) {
                    String originFileName = multipartFiles.get(j).getOriginalFilename();
                    String filePath = fileService.uploadFileV1("album", multipartFiles.get(j));

                    Picture picture = Picture.builder()
                            .album(album)
                            .origin_name(originFileName)
                            .path_name(filePath)
                            .build();
                    pictureRepository.save(picture);
                    fileService.resizeImage("album", multipartFiles.get(j), picture);
                }
            }
            //메인 사진 업데이트
            List<Picture> newPictureList = pictureRepository.findPicturesByAlbumId(album.getId());
            for (int k = 0; k < newPictureList.size(); k++) {
                if (mainIndex == k) {
                    newPictureList.get(k).updateIsMain(true);
                } else {
                    newPictureList.get(k).updateIsMain(false);
                }

            }
        }
//        //사진 수가 기존보다 많은 경우
//        if(multipartFiles.size()>=pictures.size()){
//            for(int i = 0 ; i< multipartFiles.size();i++){
//
//                String originFileName = multipartFiles.get(i).getOriginalFilename();
//
//                String filePath = fileService.uploadFileV1("album",multipartFiles.get(i));
//                if(mainIndex==i){
//                    isMain = true;
//                }
//                else {
//                    isMain = false;
//                }
//                //기존 사진 수정
//                if(i<pictures.size()){
//                    pictures.get(i).updateOriginName(originFileName);
//                    pictures.get(i).updatePathName(filePath);
//                    pictures.get(i).updateIsMain(isMain);
//                    pictureRepository.save(pictures.get(i));
//
//                }
//                else{
//                    Picture picture = Picture.builder()
//                            .album(album)
//                            .origin_name(originFileName)
//                            .path_name(filePath)
//                            .is_main(isMain)
//                            .build();
//                    pictureRepository.save(picture);
//                }
//
//            }
//        }
//        //사진 수가 기존보다 적은 경우
//        else{
//            for(int i = 0; i<pictures.size();i++){
//                if(mainIndex==i){
//                    isMain = true;
//                }
//                else {
//                    isMain = false;
//                }
//                if(i<multipartFiles.size()){
//                    String originFileName = multipartFiles.get(i).getOriginalFilename();
//                    String filePath =fileService.uploadFileV1("album",multipartFiles.get(i));
//                    pictures.get(i).updateOriginName(originFileName);
//                    pictures.get(i).updatePathName(filePath);
//                    pictures.get(i).updateIsMain(isMain);
//                    pictureRepository.save(pictures.get(i));
//                }
//                else{
//                    pictureRepository.delete(pictures.get(i));
//                }
//
//            }
//        }
    }


    @Transactional
    public void manageAlbumReaction(long userPk, Album album, AlbumReactionReqDto albumReactionReqDto) {
        Profile profile = profileRepository.findProfileByUserPk(userPk);

        AlbumReaction albumReaction =
                albumReactionRepository.findReactionByAlbumId(
                        album.getId(), profile.getId());

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
    public void deleteAlbumReaction(long userPk, long reactionId) {

        Profile profile = profileRepository.findProfileByUserPk(userPk);
        AlbumReaction albumReaction =
                albumReactionRepository.findReactionByReactionId(
                        reactionId, profile.getId());
        albumReactionRepository.delete(albumReaction);
    }

    @Transactional
    public List<AlbumReactionListResDto> findReactionList(List<AlbumReaction> albumReactions, long userPk) {

        List<AlbumReactionListResDto> result = new ArrayList<>();
        Profile profile = profileRepository.findProfileByUserPk(userPk);

        for (int i = 0; i < albumReactions.size(); i++) {
            AlbumReactionListResDto albumReactionListResDto = AlbumReactionListResDto.builder()
                    .emoticon(albumReactions.get(i).getEmoticon())
                    .imagePath(profile.getImagePath())
                    .role(profile.getRole())
                    .profileId(profile.getId())
                    .reactionId(albumReactions.get(i).getId())
                    .build();
            result.add(albumReactionListResDto);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<Album> findAlbumsByHashTag(String keyword, Long albumId) {

        return albumRepository.findAlbumByHashTag(keyword, albumId);
    }

    @Transactional(readOnly = true)
    public List<Album> findAlbumsByDate(String date, Long albumId) {

        return albumRepository.findAlbumByDate(date, albumId);
    }


}
