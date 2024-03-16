package com.lukian.onlinecarsharing.controller;

import com.lukian.onlinecarsharing.dto.RoleRequestDto;
import com.lukian.onlinecarsharing.dto.user.UserRegisterRequestDto;
import com.lukian.onlinecarsharing.dto.user.UserRegisterResponseDto;
import com.lukian.onlinecarsharing.model.User;
import com.lukian.onlinecarsharing.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management",
        description = "Endpoints for managing user and user related entities")
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}/role")
    @PreAuthorize(value = "hasRole('ROLE_MANAGER')")
    @Operation(summary = "Update user role",
            description = "updates user role based on id and provided String (manager only)")
    public UserRegisterResponseDto updateUserRole(@PathVariable Long id,
                                                  @RequestBody @Valid RoleRequestDto requestDto) {
        return userService.updateRole(id, requestDto.role());
    }

    @GetMapping("/me")
    @PreAuthorize(value = "hasRole('ROLE_CUSTOMER') or hasRole('ROLE_MANAGER')")
    @Operation(summary = "Get user info",
            description = "gets profile info of currently logged-in user")
    public UserRegisterResponseDto getUserInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.getProfileInfo(user.getId());
    }

    @PutMapping("/me")
    @PreAuthorize(value = "hasRole('ROLE_CUSTOMER') or hasRole('ROLE_MANAGER')")
    @Operation(summary = "Update user profile info",
            description = "updates profile info of current logged-in user based on request dto")
    public UserRegisterResponseDto updateProfileInfo(
            Authentication authentication,
            @RequestBody @Valid UserRegisterRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return userService.updateProFileInfo(user.getId(), requestDto);
    }
}
