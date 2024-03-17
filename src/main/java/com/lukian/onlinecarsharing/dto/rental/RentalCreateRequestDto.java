package com.lukian.onlinecarsharing.dto.rental;

import com.lukian.onlinecarsharing.validation.date.ReturnDateConstraint;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record RentalCreateRequestDto(
        @ReturnDateConstraint LocalDate returnTime,
        @NotNull Long carId,
        @NotNull Long userId
) {
}
