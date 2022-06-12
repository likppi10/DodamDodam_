package com.ssafy.core.dto.req;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@Schema(description = "DTO Model : AlbumReqDto")
public class AlbumUpdateReqDto {
    @NotNull
    @ArraySchema(schema = @Schema(description = "해시태그", required = true, example = "광화문"))
    private List<String> hashTags;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-M-d")
    @Schema(description = "앨범날짜", required = true, example = "2022-04-20")
    private LocalDate date;

    @Schema(description = "앨범아이디")
    private Long albumId;

    @Schema(description = "앨범삭제 Id List")
    private int[] pictureIdList;

    @NotNull
    @Schema(description = "메인사진 index", required = true, example = "1")
    private Long mainIndex;

    @Schema(description = "사진들")
    private List<MultipartFile> multipartFiles;
}