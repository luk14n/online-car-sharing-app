package com.lukian.onlinecarsharing.dto.user;

import com.lukian.onlinecarsharing.validation.email.EmailConstraint;
import com.lukian.onlinecarsharing.validation.password.PasswordConstraint;

public record UserLoginRequestDto(
        @EmailConstraint String email,
        @PasswordConstraint String password
) {}
