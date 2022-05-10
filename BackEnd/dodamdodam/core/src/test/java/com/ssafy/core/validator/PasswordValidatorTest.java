//package com.ssafy.core.validator;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.BDDMockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//
//@SpringBootTest
//class PasswordValidatorTest {
//
//    @Autowired
//    private Validator validatorInjected;
//
//    @Test
//    void test_must_succeed() {
//        // Given
//        UserRegisterRequestDto registerRequestDto = UserRegisterRequestDto.of("james", "test123@example.com");
//
//        // When
//        Set<ConstraintViolation<UserRegisterRequestDto>> violations = validatorInjected.validate(registerRequestDto);
//
//        // Then
//        BDDMockito.Then(violations).isNotEmpty();
//    }
//}