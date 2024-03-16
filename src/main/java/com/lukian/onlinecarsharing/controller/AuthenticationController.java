package com.lukian.onlinecarsharing.controller;

import com.lukian.onlinecarsharing.dto.user.UserLoginRequestDto;
import com.lukian.onlinecarsharing.dto.user.UserLoginResponseDto;
import com.lukian.onlinecarsharing.dto.user.UserRegisterRequestDto;
import com.lukian.onlinecarsharing.dto.user.UserRegisterResponseDto;
import com.lukian.onlinecarsharing.exception.RegistrationException;
import com.lukian.onlinecarsharing.security.AuthenticationService;
import com.lukian.onlinecarsharing.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication management",
        description = "Manages authentication related endpoints")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Registers new user",
            description = "Performs user registration by saving him into DB "
                    + "(provided he was NOT registered before) based on request DTO")
    public UserRegisterResponseDto register(
            @RequestBody @Valid UserRegisterRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    @Operation(summary = "Logs new user in",
            description = "Performs user log-in and gives JWT token")
    public UserLoginResponseDto login(
            @RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
