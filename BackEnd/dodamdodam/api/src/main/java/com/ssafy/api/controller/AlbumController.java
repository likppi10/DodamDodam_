package com.ssafy.api.controller;

import com.ssafy.core.dto.req.AlbumReactionReqDto;
import com.ssafy.core.dto.req.AlbumReqDto;
import com.ssafy.core.dto.req.AlbumUpdateReqDto;
import com.ssafy.core.dto.res.*;
import com.ssafy.api.service.AlbumService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ListResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/album")
@Tag(name = "AlbumController", description = "앨범컨트롤러")
public class AlbumController {

    private final AlbumService albumService;
    private final ResponseService responseService;

    //앨범수정, 앨범 검색
    @Operation(summary = "앨범 전체 조회", description = "<strong>앨범 전체 조회</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ListResult<AlbumResDto> getAlbums(Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());
        return responseService.getListResult(albumService.findAlbumList(userPk));
    }


    @Operation(summary = "앨범 상세 조회", description = "<strong>앨범 상세 조회</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "{albumId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<AlbumDetailResDto> getAlbum(@PathVariable Long albumId) {

        return responseService.getSingleResult(albumService.getAlbumDetail(albumId));
    }


    @Operation(summary = "앨범 등록", description = "<strong>앨범 등록</strong>", parameters = {
            @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
    })
    @PostMapping(value = "create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult createAlbum(@ModelAttribute
                                    @Valid AlbumReqDto albumReqDto,
                                    Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());
        albumService.createAlbum(albumReqDto, userPk);
        return responseService.getSuccessResult();

    }


    @Operation(summary = "앨범 리액션 등록 및 수정", description = "<strong>앨범 리액션 등록 및 수정</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @PostMapping(value = "reaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult createReaction(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody
                    AlbumReactionReqDto albumReactionReqDto,
            Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());
        albumService.manageAlbumReaction(albumReactionReqDto, userPk);
        return responseService.getSuccessResult();
    }


    @Operation(summary = "앨범 리액션 삭제", description = "<strong>앨범 리액션 삭제</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @DeleteMapping(value = "{reactionid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult deleteReaction(@PathVariable Long reactionid, Authentication authentication) {

        Long userPK = Long.parseLong(authentication.getName());
        albumService.deleteAlbumReaction(userPK, reactionid);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "앨범 수정", description = "<strong>앨범 수정</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @PatchMapping(value = "update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult updateAlbum(
            @ModelAttribute
            @Valid AlbumUpdateReqDto albumUpdateReqDto) {

        albumService.updateAlbum(albumUpdateReqDto);
        return responseService.getSuccessResult();
    }

    @DeleteMapping(value = "deleteAlbum/{albumId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "앨범 삭제", description = "<strong>앨범 삭제</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            }
    )
    public CommonResult deleteAlbum(@PathVariable Long albumId, Authentication authentication) {

        Long userPK = Long.parseLong(authentication.getName());
        albumService.deleteAlbum(albumId, userPK);
        return responseService.getSuccessResult();
    }

}
