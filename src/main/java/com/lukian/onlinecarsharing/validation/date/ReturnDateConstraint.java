package com.lukian.onlinecarsharing.validation.date;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ReturnDateValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ReturnDateConstraint {
    String message() default "Date of return can't be in the past, "
            + "it should be at least one day from today";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

