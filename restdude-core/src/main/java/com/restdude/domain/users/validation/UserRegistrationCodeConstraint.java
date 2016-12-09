package com.restdude.domain.users.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = {UserRegistrationCodeValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserRegistrationCodeConstraint {
    String message() default "{org.hibernate.validator.constraints..NotBlankOrNull.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}