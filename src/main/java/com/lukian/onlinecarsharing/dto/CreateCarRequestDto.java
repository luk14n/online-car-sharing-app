package com.lukian.onlinecarsharing.dto;

import com.lukian.onlinecarsharing.model.Car;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateCarRequestDto(
        @NotBlank String model,
        @NotBlank String brand,
        @NotNull Car.Type type,
        @NotNull @Positive int inventory,
        @NotNull @Positive @Min(1) BigDecimal dailyFee
) {}
