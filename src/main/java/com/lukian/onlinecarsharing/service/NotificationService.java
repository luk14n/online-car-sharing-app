package com.lukian.onlinecarsharing.service;

import com.lukian.onlinecarsharing.model.Car;
import com.lukian.onlinecarsharing.model.Payment;
import com.lukian.onlinecarsharing.model.Rental;
import java.util.Set;

public interface NotificationService {

    void sendMessageAboutCreatedRental(Rental rental);

    void sendMessageAboutOverdueRental(Rental rental);

    void sendMessageAboutSuccessPayment(Payment payment, Car car);

    void sendMessageAboutCanceledPayment(Payment payment, Car car);

    void sendScheduledMessageAboutOverdueRentals(Set<Rental> overdueRentals);

    void sendNoRentalsOverdueMessage();
}
