package com.ssafy.core.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Model : ProfileReqDto")
public class ProfileReqDto {

//    @Schema(value = "프로필이미지", required = false, example = "")
//    private MultipartFile profileImage;

    @NotBlank
    @Size(max = 10, min = 1)
    @Schema(description = "역할", required = true, example = "아들")
    private String role;

    @NotBlank
    @Size(max = 10, min = 1)
    @Schema(description = "닉네임", required = true, example = "싸피")
    private String nickname;

    @NotNull
    @Schema(description = "생년월일", required = true, example = "1995-08-20")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Nullable
    @Schema(description = "프로필사진")
    private MultipartFile multipartFile;

    @Nullable
    @Schema(description = "프로필사진 url")
    private String characterPath;
}
