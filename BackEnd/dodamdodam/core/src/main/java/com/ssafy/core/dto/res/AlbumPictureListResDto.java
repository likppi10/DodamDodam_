package com.ssafy.core.dto.res;

import com.ssafy.core.entity.Picture;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumPictureListResDto {
    @Schema(description = "메인여부", example = "")
    private boolean isMain;

    @Schema(description = "사진경로", example = "")
    private String imagePath;

    @Schema(description = "사진id", example = "")
    private long pictureId;

    public List<AlbumPictureListResDto> fromEntity(List<Picture> pictures){
        List<AlbumPictureListResDto> result = new ArrayList<>();
        for(int i = 0 ; i<pictures.size() ; i++) {
            AlbumPictureListResDto albumDetailResDto = AlbumPictureListResDto.builder()
                    .isMain(pictures.get(i).is_main())
                    .imagePath(pictures.get(i).getPath_name())
                    .pictureId(pictures.get(i).getId())
                    .build();
//            if(pictures.get(i).is_main()){
//                result.add(0,albumDetailResDto);
//            }else{
                result.add(albumDetailResDto);
//            }

        }
        return result;
    }
}
