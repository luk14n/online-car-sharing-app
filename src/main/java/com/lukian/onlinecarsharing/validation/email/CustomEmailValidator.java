package com.lukian.onlinecarsharing.validation.email;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomEmailValidator implements
        ConstraintValidator<EmailConstraint, String> {
    public static final String EMAIL_REGEXP = "^.+@.+\\S+$";

    @Override
    public void initialize(EmailConstraint email) {
    }

    @Override
    public boolean isValid(String email,
                           ConstraintValidatorContext cxt) {
        return email != null && email.matches(EMAIL_REGEXP)
                && (email.length() > 4) && (email.length() < 128);
    }
}
