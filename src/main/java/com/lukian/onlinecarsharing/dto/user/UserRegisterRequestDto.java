package com.lukian.onlinecarsharing.dto.user;

import com.lukian.onlinecarsharing.validation.email.EmailConstraint;
import com.lukian.onlinecarsharing.validation.password.FieldMatch;
import com.lukian.onlinecarsharing.validation.password.PasswordConstraint;
import jakarta.validation.constraints.NotBlank;

@FieldMatch.FieldMatchList({
        @FieldMatch(first = "password", second = "repeatPassword",
                message = "Passwords don't match")
})
public record UserRegisterRequestDto(
        @EmailConstraint
        String email,
        @PasswordConstraint
        String password,
        String repeatPassword,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName
) {}
