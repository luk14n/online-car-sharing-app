package com.lukian.onlinecarsharing.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomPasswordValidator implements
        ConstraintValidator<PasswordConstraint, String> {
    public static final String PASSWORD_REGEXP =
            "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]+$";

    @Override
    public void initialize(PasswordConstraint password) {
    }

    @Override
    public boolean isValid(String password,
                           ConstraintValidatorContext cxt) {
        return password != null && password.matches(PASSWORD_REGEXP)
                && (password.length() >= 8) && (password.length() < 128);
    }
}
