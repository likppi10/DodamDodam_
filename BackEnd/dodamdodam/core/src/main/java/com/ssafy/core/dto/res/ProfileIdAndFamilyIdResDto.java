package com.ssafy.core.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ProfileId And FamilyId")
public class ProfileIdAndFamilyIdResDto {

    @Schema(description = "profileId", example = "1")
    private Long profileId;

    @Schema(description = "familyId", example = "1")
    private Long familyId;
}
