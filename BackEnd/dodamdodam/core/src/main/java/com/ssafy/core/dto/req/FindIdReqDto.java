package com.ssafy.core.dto.req;

import com.ssafy.core.validator.FamilyCode;
import com.ssafy.core.validator.LocalDate;
import com.ssafy.core.validator.UserName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Model : FindIdReqDto")
public class FindIdReqDto {

    @UserName
    @Schema(description = "이름", required = true, example = "싸피")
    private String name;

    @LocalDate
    private String birthday;

    @FamilyCode
    @Schema(description = "가족 코드", required = true, example = "SSAFYFINALPROJE")
    private String familyCode;
}