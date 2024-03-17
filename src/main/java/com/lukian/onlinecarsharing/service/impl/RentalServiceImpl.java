package com.lukian.onlinecarsharing.service.impl;

import com.lukian.onlinecarsharing.dto.rental.ActualReturnDateSetRequestDto;
import com.lukian.onlinecarsharing.dto.rental.RentalCreateRequestDto;
import com.lukian.onlinecarsharing.dto.rental.RentalDto;
import com.lukian.onlinecarsharing.exception.CarIsAlreadyReturnedException;
import com.lukian.onlinecarsharing.exception.EntityNotFoundException;
import com.lukian.onlinecarsharing.exception.InvalidDateException;
import com.lukian.onlinecarsharing.exception.NoCarsLeftException;
import com.lukian.onlinecarsharing.mapper.CarMapper;
import com.lukian.onlinecarsharing.mapper.RentalMapper;
import com.lukian.onlinecarsharing.model.Car;
import com.lukian.onlinecarsharing.model.Rental;
import com.lukian.onlinecarsharing.model.User;
import com.lukian.onlinecarsharing.repository.CarRepository;
import com.lukian.onlinecarsharing.repository.RentalRepository;
import com.lukian.onlinecarsharing.repository.UserRepository;
import com.lukian.onlinecarsharing.service.CarService;
import com.lukian.onlinecarsharing.service.RentalService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final RentalMapper rentalMapper;
    private final CarService carService;
    private final CarMapper carMapper;
    private final CarRepository carRepository;

    @Override
    @Transactional
    public RentalDto save(Long userId, RentalCreateRequestDto requestDto) {
        User userFromDb = getUserFromDbById(userId);

        Car carFromDb = carMapper.toModel(carService.finById(requestDto.carId()));

        if (carFromDb.getInventory() > 1) {
            Rental rental = new Rental();
            rental.setRentalDate(LocalDate.now());
            rental.setReturnTime(requestDto.returnTime());
            rental.setCar(carFromDb);
            rental.setUser(userFromDb);

            carFromDb.setInventory(carFromDb.getInventory() - 1);
            carRepository.save(carFromDb);

            return rentalMapper.toDto(rentalRepository.save(rental));
        }
        throw new NoCarsLeftException("Sorry, given car is not available at the moment");
    }

    @Override
    public List<RentalDto> getActiveOrNot(Pageable pageable, Long userId, boolean isActive) {
        List<Rental> rentals = rentalRepository.findAllByUserId(pageable, userId);
        LocalDate now = LocalDate.now();
        return isActive
                ?
                rentals.stream()
                        .filter(r -> r.getReturnTime().isAfter(now))
                        .map(rentalMapper::toDto)
                        .toList()
                :
                rentals.stream()
                        .filter(r -> r.getReturnTime().isBefore(now)
                                || r.getReturnTime().isEqual(now))
                        .map(rentalMapper::toDto)
                        .toList();
    }

    @Override
    public List<RentalDto> findAll(Pageable pageable) {
        return rentalRepository.findAll(pageable).stream()
                .map(rentalMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public RentalDto setActualReturnDate(Long userId,
                                         ActualReturnDateSetRequestDto requestDto) {
        Rental rentalFromDB = getRentalFromDBbyUserIdAndRentalId(userId, requestDto.rentalId());

        if (rentalFromDB.getActualReturnDate() == null
                && actualReturnDateIsValid(
                        requestDto.actualReturnDate(), rentalFromDB.getRentalDate())) {

            rentalFromDB.setActualReturnDate(requestDto.actualReturnDate());

            Car carFromDb = rentalFromDB.getCar();
            carFromDb.setInventory(carFromDb.getInventory() + 1);
            carRepository.save(carFromDb);

            return rentalMapper.toDto(rentalRepository.save(rentalFromDB));
        }
        throw new CarIsAlreadyReturnedException("This car has been already returned");
    }

    @Override
    public List<RentalDto> findAllPersonal(Long userId, Pageable pageable) {
        return rentalRepository.findAllByUserId(pageable, userId).stream()
                .map(rentalMapper::toDto)
                .toList();
    }

    private boolean actualReturnDateIsValid(LocalDate actualReturnDate, LocalDate startRentalDate) {
        if (actualReturnDate.isAfter(startRentalDate)) {
            return true;
        }
        throw new InvalidDateException(
                "Actual return date should be at least one day after start of the rental");
    }

    private Rental getRentalFromDBbyUserIdAndRentalId(Long userId, Long rentalId) {
        return rentalRepository.findByIdAndUserId(rentalId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot find rental by id: " + rentalId));
    }

    private User getUserFromDbById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cannot find user by id: " + userId));
    }
}
