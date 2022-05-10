package com.ssafy.core.validator;

import com.ssafy.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

@Component
@RequiredArgsConstructor
public class UserIdUniqueValidator implements ConstraintValidator<UserIdUnique, String> {

    private final UserRepository userRepository;

    @Override
    public void initialize(UserIdUnique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String userId, ConstraintValidatorContext context) {

        final boolean isExistUserId = userRepository.existsUserByUserId(userId);

        if (isExistUserId) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    MessageFormat.format("입력하신 아이디 {0}: 이미 사용중인 아이디 입니다.", userId)
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
