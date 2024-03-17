package com.lukian.onlinecarsharing.dto.role;

import jakarta.validation.constraints.NotBlank;

public record RoleRequestDto(@NotBlank String role) {
}
