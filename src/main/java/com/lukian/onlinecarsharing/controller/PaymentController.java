package com.lukian.onlinecarsharing.controller;

import com.lukian.onlinecarsharing.dto.payment.CreatePaymentRequestDto;
import com.lukian.onlinecarsharing.dto.payment.PaymentDto;
import com.lukian.onlinecarsharing.model.User;
import com.lukian.onlinecarsharing.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@Tag(name = "Payment management", description = "Endpoints for managing cars payments")
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "Get all user's payments",
            description = "Get all user payments")
    @ResponseBody
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    public List<PaymentDto> getAll(@PageableDefault(page = 0, size = 10) Pageable pageable,
                                   Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return paymentService.getPayments(user.getId(), pageable);
    }

    @Operation(summary = "Get all payments by status",
            description = "Get all payments by status (Pageable default: page = 0, size = 10)")
    @ResponseBody
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/search/")
    public List<PaymentDto> searchPayments(@RequestParam(name = "status") String status,
                                           @PageableDefault(page = 0, size = 10) Pageable pageable,
                                           Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return paymentService.getPaymentsByStatus(user.getId(), status, pageable);
    }

    @Operation(summary = "Create session",
            description = "Session is created to enable user to use Stripe")
    @ResponseBody
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/pay")
    public PaymentDto createPaymentIntent(@RequestBody @Valid CreatePaymentRequestDto requestDto) {
        return paymentService.createPaymentSession(requestDto);
    }

    @Operation(summary = "Page redirection of successful payment",
            description = "Redirect to success page if payment finish in stripe session")
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/success")
    public String success(@RequestParam String sessionId) {
        paymentService.getSuccessfulPayment(sessionId);
        return "success";
    }

    @Operation(summary = "Page redirection of canceled payment",
            description = "Redirect to cancel page if payment finish in stripe session")
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/cancel")
    public String afterCancelPayment(@RequestParam String sessionId) {
        paymentService.getCancelledPayment(sessionId);
        return "canceled";
    }
}
