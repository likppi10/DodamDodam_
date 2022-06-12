package com.ssafy.core.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = UserNameValidator.class)
@Target({FIELD, METHOD})
@Retention(RUNTIME)
public @interface UserName {


    String message() default "입력하신 이름의 형식이 맞지 않습니다.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
