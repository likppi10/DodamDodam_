package com.ssafy.core.dto.req;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
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
public class AlbumReqDto {

    @NotNull
    @ArraySchema(schema = @Schema(description = "해시태그", required = true, example = "광화문"))
    private List<String> hashTags;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-M-d")
    @Schema(description = "앨범날짜", required = true, example = "2022-04-20")
    private LocalDate date;


    @NotNull
    @Schema(description = "메인사진 index", required = true, example = "1")
    private Long mainIndex;

    private List<MultipartFile> multipartFiles;
}
