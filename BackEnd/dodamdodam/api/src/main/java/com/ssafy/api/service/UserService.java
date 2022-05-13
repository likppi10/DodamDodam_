package com.ssafy.api.service;

import com.ssafy.api.config.JwtProvider;
import com.ssafy.core.common.ProviderType;
import com.ssafy.core.dto.req.FcmTokenReqDto;
import com.ssafy.core.dto.req.FindIdReqDto;
import com.ssafy.core.dto.req.UserInfoReqDto;
import com.ssafy.core.dto.req.SignUpReqDto;
import com.ssafy.core.dto.res.ReIssueTokenResDto;
import com.ssafy.core.dto.res.SignInResDto;
import com.ssafy.core.dto.res.SocialUserResDTO;
import com.ssafy.core.entity.User;
import com.ssafy.core.exception.ErrorCode;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.FamilyRepository;
import com.ssafy.core.repository.ProfileRepository;
import com.ssafy.core.repository.UserRepository;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static com.ssafy.core.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional(readOnly = true)
    public User findByUserPk(Long userPk) {
        return userRepository.findUserByUserPk(userPk)
                .orElseThrow(() -> new CustomException(USER_DOESNT_EXIST));
    }

    @Transactional
    public void signUp(SignUpReqDto singUpRequest) {

        this.checkExistId(singUpRequest.getUserId());

        userRepository.save(User.builder()
                .userId(singUpRequest.getUserId())
                .name(singUpRequest.getName())
                .password(passwordEncoder.encode(singUpRequest.getPassword()))
                .build());
    }

    @Transactional(readOnly = true)
    public void checkExistId(String userId) {
        if (userRepository.existsUserByUserId(userId)) {
            throw new CustomException(ErrorCode.DUPLICATE_USER_ID);
        }
    }

    @Transactional(readOnly = true)
    public String getUserIdWithUserInfo(FindIdReqDto request) {
        String userId = userRepository.findUserIdByUserInfo(
                request.getName(),
                LocalDate.parse(request.getBirthday()),
                request.getFamilyCode()
        );
        if (userId == null) {
            throw new CustomException(USER_DOESNT_EXIST);
        }
        return userId;
    }

    @Transactional
    public void updateBirthdayWithUserPk(Long userPk, String birthday) {

        User user = this.findByUserPk(userPk);

        user.updateBirthday(LocalDate.parse(birthday));

        userRepository.save(user);
    }

    @Transactional
    public void updateFcmToken(User user, FcmTokenReqDto fcmReq) {
        user.updateFcmToken(fcmReq.getFcmToken());
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Long getFamilyIdByUserPk(Long userPk) {
        Long familyId = familyRepository.findFamilyIdByUserPK(userPk);
        if (familyId == null) {
            throw new CustomException(NOT_FOUND_FAMILY);
        }
        return familyId;
    }

    @Transactional
    public ReIssueTokenResDto reissueAccessToken(String token, String refreshToken) {

        Long userPk = jwtProvider.getUserPkFromExpiredToken(token);

        if (userPk == null || !jwtProvider.validateToken(refreshToken)) {
            throw new CustomException(INVALID_TOKEN);
        }

        User user = userRepository.findUserByUserPk(userPk)
                .orElseThrow(() -> new CustomException(USER_DOESNT_EXIST));

        token = jwtProvider.createAccessToken(user);
        refreshToken = jwtProvider.createRefreshToken();

        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        return ReIssueTokenResDto.builder()
                .jwtToken(token)
                .refreshToken(refreshToken)
                .build();

    }

    @Transactional
    public void updatePassword(UserInfoReqDto request) {
        User user = userRepository.findUserByUserId(request.getUserId())
                .orElseThrow(() -> new CustomException(USER_DOESNT_EXIST));

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(INVALID_REQUEST, "재설정 비밀번호가 기존 비밀번호와 같습니다!");
        }
        user.updatePassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }

    @Transactional
    public SignInResDto localSignIn(UserInfoReqDto signInRequest) {

        User user = userRepository.findUserByUserIdAndProviderType(signInRequest.getUserId(), ProviderType.LOCAL);

        if(user == null){
            throw new CustomException(USER_DOESNT_EXIST);
        }

        if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            throw new CustomException(INVALID_REQUEST, "비밀번호를 잘못 입력하셨습니다.");
        }

        return getSignInResDto(user);
    }

    @Transactional
    public SignInResDto socialSignIn(String accessToken) {

        SocialUserResDTO socialUser = WebClient.create().get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new CustomException(SOCIAL_TOKEN_IS_NOT_VALID)))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new CustomException(INTERVAL_SERVER_ERROR)))
                .bodyToMono(SocialUserResDTO.class)
                .block();

        User user = userRepository.findUserByUserIdAndProviderType(socialUser.getId(), ProviderType.KAKAO);

        System.out.println(socialUser.getId());

        //처음이면 가입시킴.
        if (user == null) {
            user = User.builder()
                    .userId(socialUser.getId())
                    .providerType(ProviderType.KAKAO)
                    .name("KAKAO USER")
                    .build();

            user = userRepository.save(user);
        }

        return getSignInResDto(user);

    }

    private SignInResDto getSignInResDto(User user) {
        String token = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken();

        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        System.out.println(user.getUserPk());
        System.out.println(user.getUserId());

        SignInResDto userInfo =
                profileRepository.findProfileIdAndFamilyIdByUserPk(user.getUserPk());

        if (userInfo == null) {
            userInfo = new SignInResDto();
        }

        System.out.println(userInfo.getProfileId());
        System.out.println(userInfo.getFamilyId());


        userInfo.setJwtToken(token);
        userInfo.setRefreshToken(refreshToken);
        userInfo.setName(user.getName());

        return userInfo;
    }
}
