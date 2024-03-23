package com.lukian.onlinecarsharing.dto.payment;

import com.lukian.onlinecarsharing.model.Payment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreatePaymentRequestDto(
        @Positive
        Long rentalId,
        @NotNull
        Payment.PaymentType paymentType
) {
}
