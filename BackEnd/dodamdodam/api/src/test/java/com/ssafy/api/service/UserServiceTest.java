package com.ssafy.api.service;

import com.ssafy.api.config.JwtProvider;
import com.ssafy.core.dto.req.FindIdReqDto;
import com.ssafy.core.dto.req.SignUpReqDto;
import com.ssafy.core.dto.req.UserInfoReqDto;
import com.ssafy.core.dto.res.ReIssueTokenResDto;
import com.ssafy.core.dto.res.SignInResDto;
import com.ssafy.core.entity.User;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.FamilyRepository;
import com.ssafy.core.repository.ProfileRepository;
import com.ssafy.core.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Transactional
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FamilyRepository familyRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    final User defaultUser = User.builder()
            .userId("test")
            .name("test")
            .password("password1!")
            .build();

    final User haveBirthdayUser = User.builder()
            .userId("test")
            .name("test")
            .password("password1!")
            .birthday(LocalDate.parse("1992-12-04"))
            .build();

    @Test
    @DisplayName("findByUserPk 정상작동")
    void findByUserPk_success() {
        //given
        given(userRepository.findUserByUserPk(anyLong()))
                .willReturn(Optional.ofNullable(defaultUser));

        //when
        final User user = userService.findByUserPk(1L);

        //then
        then(user.getUserId()).isEqualTo("test");
        then(user.getName()).isEqualTo("test");
        then(user.getPassword()).isEqualTo("password1!");
        then(user.getBirthday()).isNull();

    }

    @Test
    @DisplayName("findByUserPk 아이디가 존재하지 않음")
    void findByUserPk_id_doesnt_exist() {
        //given
        given(userRepository.findUserByUserPk(anyLong()))
                .willReturn(Optional.empty());

        //when
        final Throwable throwable = catchThrowable(
                () -> userService.findByUserPk(1L));

        //then
        then(throwable).isInstanceOf(CustomException.class);

    }

    @Test
    @DisplayName("signUp 정상동작")
    void signUp_success() {
        //given
        SignUpReqDto testUser = SignUpReqDto.builder()
                .userId("test")
                .name("test")
                .password("password1!")
                .build();

        given(passwordEncoder.encode(anyString())).willReturn("password1!");

        given(userRepository.existsUserByUserId(anyString()))
                .willReturn(false);

        given(userRepository.save(any())).willReturn(defaultUser);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        //when
        userService.signUp(testUser);

        //then
        verify(userRepository, times(1)).save(captor.capture());
        User savedUser = captor.getValue();

        then(savedUser.getUserId()).isEqualTo(testUser.getUserId());
        then(savedUser.getPassword()).isEqualTo(testUser.getPassword());
        then(savedUser.getName()).isEqualTo(testUser.getName());

    }

    @Test
    void signUp_이미_가입된_아이디() {
        //given
        SignUpReqDto testUser = SignUpReqDto.builder()
                .userId("test")
                .name("test")
                .password("password1!")
                .build();


        given(userRepository.existsUserByUserId(anyString()))
                .willReturn(true);

        //when
        final Throwable throwable = catchThrowable(
                () -> userService.signUp(testUser));

        //then
        then(throwable).isExactlyInstanceOf(CustomException.class);
    }

    @Test
    void getUserIdWithUserInfo_정상동작() {
        //given
        FindIdReqDto givenDto = FindIdReqDto.builder()
                .name("test")
                .birthday("1992-12-04")
                .familyCode("123451234512345")
                .build();

        given(userRepository.findUserIdByUserInfo(anyString(), any(), anyString()))
                .willReturn("test");

        //when
        String expectUserId = userService.getUserIdWithUserInfo(givenDto);

        //then
        then(expectUserId).isEqualTo("test");
    }

    @Test
    void getUserIdWithUserInfo_유저가_없을경우() {
        //given
        FindIdReqDto givenDto = FindIdReqDto.builder()
                .name("test")
                .birthday("1992-12-04")
                .familyCode("123451234512345")
                .build();

        given(userRepository.findUserIdByUserInfo(anyString(), any(), anyString()))
                .willReturn(null);

        //when
        final Throwable throwable = catchThrowable(
                () -> userService.getUserIdWithUserInfo(givenDto));

        //then
        then(throwable).isExactlyInstanceOf(CustomException.class);
    }

    @Test
    void updateBirthdayWithUserPk_정상동작() {
        //given
        given(userRepository.findUserByUserPk(anyLong()))
                .willReturn(Optional.ofNullable(haveBirthdayUser));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        //when
        userService.updateBirthdayWithUserPk(1L, "1992-12-04");

        //then
        verify(userRepository, times(1)).save(captor.capture());
        User savedUser = captor.getValue();

        then(savedUser.getUserId()).isEqualTo(haveBirthdayUser.getUserId());
        then(savedUser.getPassword()).isEqualTo(haveBirthdayUser.getPassword());
        then(savedUser.getName()).isEqualTo(haveBirthdayUser.getName());
        then(savedUser.getBirthday()).isEqualTo(haveBirthdayUser.getBirthday());

    }

//    @Test
//    void updateFcmToken() {
//    }

    @Test
    void getFamilyIdByUserPk_정상동작() {
        Long expectFamilyId = 1L;

        //given
        given(familyRepository.findFamilyIdByUserPK(anyLong()))
                .willReturn(expectFamilyId);

        //when
        Long actualFamilyId = userService.getFamilyIdByUserPk(1L);

        //then
        then(actualFamilyId).isEqualTo(expectFamilyId);
    }

    @Test
    void getFamilyIdByUserPk_패밀리가_없을때() {
        Long expectFamilyId = null;

        //given
        given(familyRepository.findFamilyIdByUserPK(anyLong()))
                .willReturn(expectFamilyId);

        //when
        final Throwable throwable = catchThrowable(
                () -> userService.getFamilyIdByUserPk(1L));

        //then
        then(throwable).isExactlyInstanceOf(CustomException.class);
    }

    @Test
    void signIn_정상동작() {
        //given
        UserInfoReqDto givenReqDto = UserInfoReqDto.builder()
                .userId("test")
                .password("password")
                .build();

        SignInResDto expectResDto = SignInResDto.builder()
                .familyId(1L)
                .profileId(1L)
                .build();

        given(userRepository.findUserIdAndProviderType(anyString(),any()))
                .willReturn(defaultUser);

        given(passwordEncoder.matches(any(CharSequence.class), anyString()))
                .willReturn(true);

        given(jwtProvider.createAccessToken(any()))
                .willReturn("token");

        given(jwtProvider.createRefreshToken()).willReturn("refreshToken");

        given(userRepository.save(any())).willReturn(defaultUser);

        given(profileRepository.findProfileIdAndFamilyIdByUserPk(any()))
                .willReturn(expectResDto);

        //when
        SignInResDto actualResDto = userService.localSignIn(givenReqDto);

        //then
        then(actualResDto.getFamilyId()).isEqualTo(expectResDto.getFamilyId());
        then(actualResDto.getProfileId()).isEqualTo(expectResDto.getProfileId());
        then(actualResDto.getName()).isEqualTo(defaultUser.getName());
        then(actualResDto.getJwtToken()).isEqualTo("token");
        then(actualResDto.getRefreshToken()).isEqualTo("refreshToken");
    }

    @Test
    void signIn_가입하지_않은_유저() {
        //given
        UserInfoReqDto givenReqDto = UserInfoReqDto.builder()
                .userId("test")
                .password("password")
                .build();

        given(userRepository.findUserIdAndProviderType(anyString(),any()))
                .willReturn(null);

        //when
        final Throwable throwable =
                catchThrowable(() -> userService.localSignIn(givenReqDto));

        //then
        then(throwable).isExactlyInstanceOf(CustomException.class);
    }

    @Test
    void signIn_비밀번호_잘못입력() {
        //given
        UserInfoReqDto givenReqDto = UserInfoReqDto.builder()
                .userId("test")
                .password("password")
                .build();

        given(userRepository.findUserIdAndProviderType(anyString(),any()))
                .willReturn(defaultUser);

        given(passwordEncoder.matches(any(), anyString()))
                .willReturn(false);


        //when
        final Throwable throwable =
                catchThrowable(() -> userService.localSignIn(givenReqDto));

        //then
        then(throwable).isExactlyInstanceOf(CustomException.class);
    }

    @Test
    void signIn_가족그룹_가입안함() {
        //given
        UserInfoReqDto givenReqDto = UserInfoReqDto.builder()
                .userId("test")
                .password("password")
                .build();

        given(userRepository.findUserIdAndProviderType(anyString(),any()))
                .willReturn(defaultUser);

        given(passwordEncoder.matches(any(), anyString()))
                .willReturn(true);

        given(jwtProvider.createAccessToken(any()))
                .willReturn("token");

        given(jwtProvider.createRefreshToken()).willReturn("refreshToken");

        given(userRepository.save(any())).willReturn(defaultUser);

        given(profileRepository.findProfileIdAndFamilyIdByUserPk(any()))
                .willReturn(null);

        //when
        SignInResDto actualResDto = userService.localSignIn(givenReqDto);

        //then
        then(actualResDto.getFamilyId()).isNull();
        then(actualResDto.getProfileId()).isNull();
        then(actualResDto.getName()).isEqualTo(defaultUser.getName());
        then(actualResDto.getJwtToken()).isEqualTo("token");
        then(actualResDto.getRefreshToken()).isEqualTo("refreshToken");
    }

    @Test
    void reissueAccessToken_정상동작() {
        //given
        Long givenUserPk = 1L;

        given(jwtProvider.getUserPkFromExpiredToken(anyString()))
                .willReturn(givenUserPk);
        given(jwtProvider.validateToken(anyString())).willReturn(true);

        given(userRepository.findUserByUserPk(anyLong()))
                .willReturn(Optional.ofNullable(defaultUser));

        given(jwtProvider.createAccessToken(any()))
                .willReturn("token");

        given(jwtProvider.createRefreshToken()).willReturn("refreshToken");

        given(userRepository.save(any())).willReturn(defaultUser);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        //when
        ReIssueTokenResDto actualResDto = userService.reissueAccessToken("t", "r");
        verify(userRepository, times(1)).save(captor.capture());
        User savedUser = captor.getValue();

        //then
        then(actualResDto.getJwtToken()).isEqualTo("token");
        then(actualResDto.getRefreshToken()).isEqualTo("refreshToken");
        then(savedUser.getRefreshToken()).isEqualTo("refreshToken");
    }

    @Test
    void reissueAccessToken_토큰에서_유저를_못찾았을때() {
        //given
        Long givenUserPk = null;

        given(jwtProvider.getUserPkFromExpiredToken(anyString()))
                .willReturn(givenUserPk);

        //when
        final Throwable throwable =
                catchThrowable(() -> userService.reissueAccessToken("t", "r"));

        //then
        then(throwable).isExactlyInstanceOf(CustomException.class);
    }

    @Test
    void reissueAccessToken_리프레쉬토큰이_유효하지_않을경우() {
        //given
        Long givenUserPk = 1L;

        given(jwtProvider.getUserPkFromExpiredToken(anyString()))
                .willReturn(givenUserPk);
        given(jwtProvider.validateToken(anyString())).willReturn(false);

        //when
        final Throwable throwable =
                catchThrowable(() -> userService.reissueAccessToken("t", "r"));

        //then
        then(throwable).isExactlyInstanceOf(CustomException.class);
    }

    @Test
    void reissueAccessToken_유저PK에_해당하는_유저가_없는경우() {
        //given
        Long givenUserPk = 1L;

        given(jwtProvider.getUserPkFromExpiredToken(anyString()))
                .willReturn(givenUserPk);
        given(jwtProvider.validateToken(anyString())).willReturn(true);

        given(userRepository.findUserByUserPk(anyLong()))
                .willReturn(Optional.empty());

        //when
        final Throwable throwable =
                catchThrowable(() -> userService.reissueAccessToken("t", "r"));

        //then
        then(throwable).isExactlyInstanceOf(CustomException.class);
    }

    @Test
    void updatePassword_정상작동() {
        //given
        UserInfoReqDto givenReqDto = UserInfoReqDto.builder()
                .userId("test")
                .password("newPassword")
                .build();

        given(userRepository.findUserByUserId(anyString()))
                .willReturn(Optional.ofNullable(defaultUser));

        given(passwordEncoder.matches(any(), anyString()))
                .willReturn(false);

        given(passwordEncoder.encode(anyString())).willReturn("newPassword");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        //when
        userService.updatePassword(givenReqDto);
        verify(userRepository, times(1)).save(captor.capture());
        User savedUser = captor.getValue();

        //then
        then(savedUser.getPassword()).isEqualTo(givenReqDto.getPassword());
    }

    @Test
    void updatePassword_아이디가_없는_경우() {
        //given
        UserInfoReqDto givenReqDto = UserInfoReqDto.builder()
                .userId("test")
                .password("newPassword")
                .build();

        given(userRepository.findUserByUserId(anyString()))
                .willReturn(Optional.empty());

        //when
        final Throwable throwable =
                catchThrowable(() -> userService.updatePassword(givenReqDto));

        //then
        then(throwable).isExactlyInstanceOf(CustomException.class);
    }

    @Test
    void updatePassword_기존_비밀번호와_같은경우() {
        //given
        UserInfoReqDto givenReqDto = UserInfoReqDto.builder()
                .userId("test")
                .password("newPassword")
                .build();

        given(userRepository.findUserByUserId(anyString()))
                .willReturn(Optional.ofNullable(defaultUser));

        given(passwordEncoder.matches(any(), anyString()))
                .willReturn(true);

        //when
        final Throwable throwable =
                catchThrowable(() -> userService.updatePassword(givenReqDto));

        //then
        then(throwable).isExactlyInstanceOf(CustomException.class);
    }
}