package com.ssafy.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ssafy.core.common.Validate.USER_ID_MAX;
import static com.ssafy.core.common.Validate.USER_ID_MIN;

public class UserIdValidator implements ConstraintValidator<UserId, String> {

    @Override
    public void initialize(UserId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String userId, ConstraintValidatorContext context) {

        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
        Matcher matcher = pattern.matcher(userId);

       if (!matcher.matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("아이디는 영문, 숫자로만 이루어져야 합니다.")
                    .addConstraintViolation();
            return false;
        } else if (userId.isBlank() || userId.length() < USER_ID_MIN || userId.length() > USER_ID_MAX) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    MessageFormat.format("아이디는 최소 {0}자 이상, 최대 {1}자 이하여야 합니다.", USER_ID_MIN, USER_ID_MAX)
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
