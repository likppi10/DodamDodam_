package com.ssafy.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

public class UserNameValidator implements ConstraintValidator<UserName, String> {

    final int USER_NAME_MIN = 2;
    final int USER_NAME_MAX = 10;

    @Override
    public void initialize(UserName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext context) {

        if (userName == null) {

            return false;
        } else if (userName.length() < USER_NAME_MIN || userName.length() > USER_NAME_MAX) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    MessageFormat.format("이름은 최소 {0}자 이상, 최대 {1}자 이하여야 합니다.", USER_NAME_MIN, USER_NAME_MAX)
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
