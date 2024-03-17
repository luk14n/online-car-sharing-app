package com.lukian.onlinecarsharing.dto.rental;

import java.time.LocalDate;

public record RentalDto(
        Long id,
        LocalDate rentalDate,
        LocalDate returnTime,
        LocalDate actualReturnDate,
        Long carId,
        Long userId
) {
}
