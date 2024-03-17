package com.lukian.onlinecarsharing.controller;

import com.lukian.onlinecarsharing.dto.rental.ActualReturnDateSetRequestDto;
import com.lukian.onlinecarsharing.dto.rental.RentalCreateRequestDto;
import com.lukian.onlinecarsharing.dto.rental.RentalDto;
import com.lukian.onlinecarsharing.model.User;
import com.lukian.onlinecarsharing.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@Tag(name = "Rentals Management",
        description = "Endpoints for rentals management")
public class RentalsController {
    private final RentalService rentalService;

    @PostMapping
    @PreAuthorize(value = "hasRole('ROLE_MANAGER') or hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "Create new rental",
            description = "Creates new rental and subtracts car from inventory")
    public RentalDto createRental(Authentication authentication,
                                  @RequestBody @Valid RentalCreateRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return rentalService.save(user.getId(), requestDto);
    }

    @GetMapping("/search/")
    @PreAuthorize(value = "hasRole('ROLE_MANAGER')")
    @Operation(summary = "Get active or not active rentals",
            description = "Retrieves rentals based on status (active or not)"
                    + " and user ID parameters")
    public List<RentalDto> getActiveOrNotRentals(
            Pageable pageable,
            @RequestParam(name = "user_id", required = false) Long userId,
            @RequestParam(name = "is_active", required = false) Boolean isActive) {
        return rentalService.getActiveOrNot(pageable, userId, isActive);
    }

    @GetMapping("/search/me/")
    @PreAuthorize(value = "hasRole('ROLE_CUSTOMER') or hasRole('ROLE_MANAGER')")
    @Operation(summary = "Get active or not active rentals",
            description = "Retrieves rentals based on status (active or not)"
                    + " and user ID parameters")
    public List<RentalDto> getPersonalActiveOrNotRentals(
            Authentication authentication,
            Pageable pageable,
            @RequestParam(name = "is_active") boolean isActive) {
        User user= (User) authentication.getPrincipal();
        return rentalService.getPersonalActiveOrNot(user.getId(), pageable, isActive);
    }

    @GetMapping
    @PreAuthorize(value = "hasRole('ROLE_MANAGER')")
    @Operation(summary = "Get all rentals",
            description = "Retrieves all rentals with pagination (manager privilege only)")
    public List<RentalDto> getAllRentals(Pageable pageable) {
        return rentalService.findAll(pageable);
    }

    @GetMapping("/me")
    @PreAuthorize(value = "hasRole('ROLE_MANAGER') or hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "Get all personal rentals",
            description = "Retrieve all personal rentals "
                    + "for the current logged-in user with pagination")
    public List<RentalDto> getAllPersonalRentals(Authentication authentication,
                                                 Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return rentalService.findAllPersonal(user.getId(), pageable);
    }

    @PostMapping("/return")
    @PreAuthorize(value = "hasRole('ROLE_MANAGER')")
    @Operation(summary = "Set actual return date",
            description = "Sets actual return date of the rental "
                    + "and adds car back to the inventory")
    public RentalDto addActualReturnDate(
            Authentication authentication,
            @RequestBody @Valid ActualReturnDateSetRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return rentalService.setActualReturnDate(user.getId(), requestDto);
    }
}
