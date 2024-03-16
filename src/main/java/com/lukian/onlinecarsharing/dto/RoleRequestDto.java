package com.lukian.onlinecarsharing.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleRequestDto(@NotBlank String role) {
}
