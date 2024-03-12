package com.lukian.onlinecarsharing.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCarRequestDto(
        @NotNull @Positive int inventory
) {}
