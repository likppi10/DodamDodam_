package com.ssafy.core.dto.req;

import com.ssafy.core.validator.FamilyCode;
import com.ssafy.core.validator.UserName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @FamilyCode
    @Schema(description = "가족 코드", required = true, example = "SSAFYFINALPROJE")
    private String familyCode;
}