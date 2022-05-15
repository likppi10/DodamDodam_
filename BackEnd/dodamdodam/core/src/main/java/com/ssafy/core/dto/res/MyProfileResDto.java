package com.ssafy.core.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "My Profile Response")
public class MyProfileResDto {

    @Schema(description = "프로필 이미지 경로", example = "example/example1.png")
    private String imagePath;

    @Schema(description = "프로필 역할", example = "아빠")
    private String role;

    @Schema(description = "닉네임", example = "싸피")
    private String nickname;

    @Schema(description = "생년월일", example = "1995-08-20")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birthday;

}
