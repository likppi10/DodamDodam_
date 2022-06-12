package com.ssafy.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FamilyCodeValidator implements ConstraintValidator<FamilyCode, String> {

    @Override
    public void initialize(FamilyCode constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String familyCode, ConstraintValidatorContext context) {

        Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d).{15}$");
        Matcher matcher = pattern.matcher(familyCode);

        if (!matcher.matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "가족코드가 형식에 맞지 않습니다."
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
