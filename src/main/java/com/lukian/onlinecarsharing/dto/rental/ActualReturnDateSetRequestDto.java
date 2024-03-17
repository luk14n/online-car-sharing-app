package com.lukian.onlinecarsharing.dto.rental;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record ActualReturnDateSetRequestDto(
        @NotNull @DateTimeFormat(pattern = DATE_REGEXP)
        LocalDate actualReturnDate,
        @NotNull Long rentalId
) {
    public static final String DATE_REGEXP = "yyyy-MM-dd";
}
