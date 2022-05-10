package com.ssafy.core.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class FcmMessageResDto {
    private boolean validate_only;
    private Message message;
    private data data;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private Notification notification;
        private  String token;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }


    @Builder
    @AllArgsConstructor
    @Getter
    public static class data {
        private String title;
        private String body;
    }
}
