package com.ssafy.api.service;

import com.ssafy.core.dto.req.SignUpReqDto;
import com.ssafy.core.entity.User;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:/application-test.yml")
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    final User defaultUser = User.builder()
            .userId("test")
            .name("test")
            .password("password1!")
            .build();

    @Test
    void findByUserPk_정상입력() {
        //given
        given(userRepository.findUserByUserPk(any(Long.class)))
                .willReturn(Optional.of(defaultUser));

        //when
        final User user = userService.findByUserPk(1L);

        //then
        then(user.getUserId()).isEqualTo("test");
        then(user.getName()).isEqualTo("test");
        then(user.getPassword()).isEqualTo("password1!");
        then(user.getBirthday()).isNull();

    }

    @Test
    void findByUserPk_아이디_존재하지_않음() {
        //given
        given(userRepository.findUserByUserPk(any(Long.class)))
                .willReturn(Optional.empty());

        //when
        final Throwable throwable = catchThrowable(
                () -> userService.findByUserPk(1L));

        //then
        then(throwable).isInstanceOf(CustomException.class);

    }

    @Test
    void signUp_정상입력() {
        //given
        SignUpReqDto input = SignUpReqDto.builder()
                .userId("test")
                .name("test")
                .password("password1!")
                .build();

        given(userRepository.existsUserByUserId(any(String.class)))
                .willReturn(false);

        given(userRepository.save(any(User.class))).willReturn(defaultUser);

        //when
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        userService.signUp(input);

        //then
        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();
        Boolean isMatch = passwordEncoder.matches("password1!", savedUser.getPassword());

        then(savedUser.getUserId()).isEqualTo("test");
        then(isMatch).isTrue();
        then(savedUser.getName()).isEqualTo("test");
        then(savedUser.getUserPk()).isNull();

    }

    @Test
    void signUp_이미_가입된_아이디() {
        //given
        SignUpReqDto input = SignUpReqDto.builder()
                .userId("test")
                .name("test")
                .password("password1!")
                .build();

        given(userRepository.existsUserByUserId(any(String.class)))
                .willReturn(true);

        given(userRepository.save(any(User.class))).willReturn(defaultUser);

        //when
        final Throwable throwable = catchThrowable(
                () -> userService.signUp(input));

        //then
//        then(throwable).isInstanceOf(CustomException.class).extracting(ErrorCode)
//                .isIn(ErrorCode.DUPLICATE_USER_ID);
    }

    @Test
    void checkExistId() {
    }

    @Test
    void findUserIdWithUserInfo() {
    }

    @Test
    void updateBirthdayWithUserPk() {
    }

    @Test
    void updateFcmToken() {
    }

    @Test
    void getFamilyIdByUserPk() {
    }

    @Test
    void signIn() {
    }

    @Test
    void reissueAccessToken() {
    }

    @Test
    void updatePassword() {
    }
}