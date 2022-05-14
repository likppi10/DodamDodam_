package com.ssafy.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.Message;
import com.ssafy.core.dto.res.FcmMessageResDto;
import com.ssafy.core.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmService {
    @Value("${fcm.key.scope}")
    private String fireBaseScope;
    @Value("${fcm.key.path}")
    private String firebasePath;
    private final ObjectMapper objectMapper;

    private final ProfileRepository profileRepository;

    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(fireBaseScope)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();
        Response response = client.newCall(request)
                .execute();
        System.out.println(response.body().string());
    }

//    public void sendMessagesToMany(String[] targetToken, String title, String body) throws IOException {
////        String token = "cMzARCj6hVZZjIXv-LZTsZ:APA91bEL0L-lJTP_MH9a_-IAptkIP-L7ERO3Yser0OwglCIIlTePEg5z9sTodOiBm8KA3QspkC-y_aEZ4BfR2oQ9nzkRmlG06hdzQ381nTMpeRm03quOPdhyoec8v_x3pcLN-pNl9Y91";
//        String message = makeMessage(targetToken, title, body);
//        OkHttpClient client = new OkHttpClient();
//        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
//        Request request = new Request.Builder()
//                .url(fireBaseScope)
//                .post(requestBody)
//                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
//                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
//                .build();
//        Response response = client.newCall(request)
//                .execute();
//        System.out.println(response.body().string());
//    }

    private String makeMessage(String token, String title, String body)
            throws JsonProcessingException {
        FcmMessageResDto fcmMessage = FcmMessageResDto.builder()
                .message(FcmMessageResDto.Message.builder()
                        .token(token)
//                        .notification(FcmMessageResDto.Notification.builder()
//                                .title(title)
//                                .body(body)
//                                .image(null)
//                                .build()
//                        )
                        .data(FcmMessageResDto.data.builder().body(body).title(title).build())
                        .build()
                ).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }
    private String getAccessToken() throws IOException {

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebasePath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    public String findFcmTokenByProfileId(Long profileId) {
        return profileRepository.findFcmTokenByProfileId(profileId);
    }
}
