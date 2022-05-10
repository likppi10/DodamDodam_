package com.ssafy.core.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = UserIdUniqueValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface UserIdUnique {

    String message() default "이미 사용중인 아이디입니다.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
