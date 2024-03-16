package com.lukian.onlinecarsharing.validation.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldMatchValidator.class)
@Documented
public @interface FieldMatch {
    String message() default "Fields do not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String first();
    String second();

    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface FieldMatchList {
        FieldMatch[] value();
    }
}
