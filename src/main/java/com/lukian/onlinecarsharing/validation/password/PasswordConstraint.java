package com.lukian.onlinecarsharing.validation.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CustomPasswordValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {
    String message() default " must be at least 8 characters long "
            + "and include one number, one letter, and one special character.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
