package com.ssafy.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class LocalDateValidator implements ConstraintValidator<LocalDate, String> {

    @Override
    public void initialize(LocalDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String localDate, ConstraintValidatorContext context) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            format.parse(localDate);
            return true;
        } catch (ParseException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "잘못된 날짜를 입력하셨습니다."
            ).addConstraintViolation();
            return false;
        }
    }
}
