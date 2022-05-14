package com.ssafy.api.service;

import com.ssafy.api.config.jwt.JwtProvider;
import com.ssafy.core.common.ProviderType;
import com.ssafy.core.dto.req.FcmTokenReqDto;
import com.ssafy.core.dto.req.FindIdReqDto;
import com.ssafy.core.dto.req.UserInfoReqDto;
import com.ssafy.core.dto.req.SignUpReqDto;
import com.ssafy.core.dto.res.ProfileIdAndFamilyIdResDto;
import com.ssafy.core.dto.res.ReIssueTokenResDto;
import com.ssafy.core.dto.res.SignInResDto;
import com.ssafy.core.dto.res.SocialUserResDTO;
import com.ssafy.core.entity.User;
import com.ssafy.core.exception.ErrorCode;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.FamilyRepository;
import com.ssafy.core.repository.ProfileRepository;
import com.ssafy.core.repository.UserRepository;
import org.springframework.lang.Nullable;
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
    public User getUserByUserPk(Long userPk) {

        return userRepository.findUserByUserPk(userPk)
                .orElseThrow(() -> new CustomException(USER_DOESNT_EXIST));
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
                request.getBirthday(),
                request.getFamilyCode());
        if (userId == null) {
            throw new CustomException(USER_DOESNT_EXIST);
        }
        return userId;
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

    @Transactional
    public void updateBirthdayByUserPk(Long userPk, LocalDate birthday) {

        User user = this.getUserByUserPk(userPk);
        user.updateBirthday(birthday);
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

        User user = this.getUserByUserPk(userPk);

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

        SocialUserResDTO socialUser = getSocialUserResDTO(accessToken);
        if(socialUser== null){
            throw new CustomException(SOCIAL_TOKEN_IS_NOT_VALID);
        }

        User user = userRepository.findUserByUserIdAndProviderType(socialUser.getId(), ProviderType.KAKAO);

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

    @Nullable
    private SocialUserResDTO getSocialUserResDTO(String accessToken) {

        return WebClient.create().get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new CustomException(SOCIAL_TOKEN_IS_NOT_VALID)))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new CustomException(INTERVAL_SERVER_ERROR)))
                .bodyToMono(SocialUserResDTO.class)
                .block();
    }

    private SignInResDto getSignInResDto(User user) {

        String token = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken();

        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        ProfileIdAndFamilyIdResDto Ids =
                profileRepository.findProfileIdAndFamilyIdByUserPk(user.getUserPk());

        //profile Id와 family Id는 둘 다 존재하거나, 둘다 없음.
        if(Ids == null){
            return SignInResDto.builder()
                    .jwtToken(token)
                    .refreshToken(refreshToken)
                    .name(user.getName())
                    .build();
        }

        return SignInResDto.builder()
                .profileId(Ids.getProfileId())
                .familyId(Ids.getFamilyId())
                .jwtToken(token)
                .refreshToken(refreshToken)
                .name(user.getName())
                .build();
    }

    @Transactional
    public void signOut(Long userPk) {

        User user = this.getUserByUserPk(userPk);

        user.updateFcmToken(null);
        userRepository.save(user);
    }
}
