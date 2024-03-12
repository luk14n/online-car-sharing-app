package com.lukian.onlinecarsharing.dto;

import com.lukian.onlinecarsharing.model.Car;
import java.math.BigDecimal;

public record CarDto(
        Long id,
        String model,
        String brand,
        Car.Type type,
        int inventory,
        BigDecimal dailyFee
) {}
