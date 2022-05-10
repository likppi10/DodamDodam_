package com.ssafy.core.dto.req;

import com.ssafy.core.validator.Password;
import com.ssafy.core.validator.UserId;
import com.ssafy.core.validator.UserIdUnique;
import com.ssafy.core.validator.UserName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Model : SignUpReqDto")
public class SignUpReqDto {

    @UserId
    @Schema(description = "아이디", required = true, example = "ssafy")
    private String userId;

    @Password
    @Schema(description = "비밀번호", required = true, example = "ssafy61!")
    private String password;

    @UserName
    @Schema(description = "이름", required = true, example = "싸피")
    private String name;

}