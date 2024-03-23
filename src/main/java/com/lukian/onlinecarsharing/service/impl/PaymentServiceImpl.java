package com.lukian.onlinecarsharing.service.impl;

import com.lukian.onlinecarsharing.dto.payment.CreatePaymentRequestDto;
import com.lukian.onlinecarsharing.dto.payment.PaymentDto;
import com.lukian.onlinecarsharing.exception.EntityNotFoundException;
import com.lukian.onlinecarsharing.mapper.PaymentMapper;
import com.lukian.onlinecarsharing.model.Car;
import com.lukian.onlinecarsharing.model.Payment;
import com.lukian.onlinecarsharing.model.Rental;
import com.lukian.onlinecarsharing.repository.PaymentRepository;
import com.lukian.onlinecarsharing.repository.RentalRepository;
import com.lukian.onlinecarsharing.service.NotificationService;
import com.lukian.onlinecarsharing.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final double FINE_MULTIPLIER = 1.5;
    private static final String DOMAIN = "http://localhost:8080";
    private static final String SUCCESS_URL = "/payments/success?sessionId={CHECKOUT_SESSION_ID}";
    private static final String CANCEL_URL = "/payments/cancel?sessionId={CHECKOUT_SESSION_ID}";

    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final PaymentMapper paymentMapper;
    private final NotificationService notificationService;

    @Value("${stripe.secret-key}")
    private String stripeKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeKey;
    }

    @Override
    public List<PaymentDto> getPayments(Long userId, Pageable pageable) {
        return paymentRepository.findAllByRentalUserId(userId, pageable).stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public List<PaymentDto> getPaymentsByStatus(Long userId, String status, Pageable pageable) {
        return paymentRepository
                .findAllByStatus(Payment.PaymentStatus.valueOf(status.toUpperCase()))
                .stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public PaymentDto createPaymentSession(CreatePaymentRequestDto request) {
        Optional<Payment> paymentFromDb = paymentRepository.findAllByRentalId(request.rentalId())
                .stream()
                .filter(p -> p.getType() == request.paymentType())
                .filter(p -> p.getStatus() != Payment.PaymentStatus.CANCELED)
                .findFirst();

        if (paymentFromDb.isPresent()) {
            Payment payment = paymentFromDb.get();
            if (payment.getStatus() == Payment.PaymentStatus.PAID) {
                throw new EntityNotFoundException("This rental has been paid");
            }
            if (payment.getStatus() == Payment.PaymentStatus.PENDING) {
                return paymentMapper.toDto(payment);
            }
        }

        Payment payment = new Payment();
        Rental rental = rentalRepository
                .findById(request.rentalId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find rental by id "
                        + request.rentalId()));
        payment.setRental(rental);
        payment.setType(request.paymentType());

        Car car = rental.getCar();
        BigDecimal dailyFee = car.getDailyFee();
        long days;
        if (request.paymentType() == Payment.PaymentType.PAYMENT) {
            LocalDateTime rentalDateTime = rental.getRentalDate().atStartOfDay();
            LocalDateTime returnDateTime = rental.getReturnTime().atStartOfDay();
            Duration duration = Duration.between(rentalDateTime, returnDateTime);
            days = duration.toDays() + 1;
        } else {
            LocalDateTime returnDateTime = rental.getReturnTime().atStartOfDay();
            LocalDateTime actualReturnDateTime = rental.getActualReturnDate().atStartOfDay();
            Duration duration = Duration.between(returnDateTime, actualReturnDateTime);
            dailyFee = dailyFee.multiply(BigDecimal.valueOf(FINE_MULTIPLIER));
            days = duration.toDays() + 1;
        }

        BigDecimal amountToPay = dailyFee.multiply(BigDecimal.valueOf(days));

        payment.setAmountToPay(amountToPay);
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment = checkout(car, payment);
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    private Payment checkout(Car car, Payment payment) {
        SessionCreateParams.Builder builder = new
                SessionCreateParams.Builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setExpiresAt(Instant.now().plus(31, ChronoUnit.MINUTES)
                        .getEpochSecond())
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(payment.getAmountToPay().longValue() * 100L)
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData
                                                .builder()
                                                .setName("Renting " + car.getBrand()
                                                        + " " + car.getModel())
                                                .build())
                                .build()
                        ).setQuantity(1L)
                        .build()
                ).setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(DOMAIN + SUCCESS_URL)
                .setCancelUrl(DOMAIN + CANCEL_URL);
        Session session;
        try {
            session = Session.create(builder.build());
            payment.setSessionId(session.getId());
            payment.setSessionUrl(new URL(session.getUrl()));
        } catch (StripeException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return payment;
    }

    @Transactional
    @Override
    public PaymentDto getSuccessfulPayment(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Cannot find session by id " + sessionId));
        payment.setStatus(Payment.PaymentStatus.PAID);
        notificationService.sendMessageAboutSuccessPayment(payment, payment.getRental().getCar());
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    @Override
    @Transactional
    public PaymentDto getCancelledPayment(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Cannot find session by id " + sessionId));
        payment.setStatus(Payment.PaymentStatus.CANCELED);
        notificationService.sendMessageAboutCanceledPayment(payment, payment.getRental().getCar());
        return paymentMapper.toDto(paymentRepository.save(payment));
    }
}
