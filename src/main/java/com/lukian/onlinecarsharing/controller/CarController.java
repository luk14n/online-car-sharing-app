package com.lukian.onlinecarsharing.controller;

import com.lukian.onlinecarsharing.dto.car.CarDto;
import com.lukian.onlinecarsharing.dto.car.CreateCarRequestDto;
import com.lukian.onlinecarsharing.dto.car.UpdateCarRequestDto;
import com.lukian.onlinecarsharing.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cars")
@RequiredArgsConstructor
@Tag(name = "Car Management",
        description = "Endpoints for managing cars in the online car sharing platform")
public class CarController {
    private final CarService carService;

    @PostMapping
    @Operation(summary = "Create a new car",
            description = "Add a new car to the car sharing platform")
    public CarDto createCar(@Valid @RequestBody CreateCarRequestDto requestDto) {
        return carService.save(requestDto);
    }

    @GetMapping
    @Operation(summary = "Get all cars",
            description = "Get a list of all available cars with optional pagination")
    public List<CarDto> getAllCars(Pageable pageable) {
        return carService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get car by ID",
            description = "Get a specific car based on its unique ID")
    public CarDto getCarById(@PathVariable Long id) {
        return carService.finById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update the car",
            description = "Update an existing car by specifying new desired parameters")
    public CarDto updateCarById(@PathVariable Long id,
                                @Valid @RequestBody CreateCarRequestDto requestDto) {
        return carService.updateCarById(id, requestDto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update car inventory",
            description = "Update the inventory of a car based on its ID")
    public CarDto updateCarInventoryById(@PathVariable Long id,
                                         @Valid @RequestBody UpdateCarRequestDto requestDto) {
        return carService.updateCarInventoryById(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the car",
            description = "Delete a specific car from the car sharing platform by its unique ID")
    public ResponseEntity<Void> deleteCarById(@PathVariable Long id) {
        carService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

