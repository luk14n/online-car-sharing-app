package com.lukian.onlinecarsharing.service.impl;

import com.lukian.onlinecarsharing.dto.car.CarDto;
import com.lukian.onlinecarsharing.dto.car.CreateCarRequestDto;
import com.lukian.onlinecarsharing.dto.car.UpdateCarRequestDto;
import com.lukian.onlinecarsharing.exception.EntityNotFoundException;
import com.lukian.onlinecarsharing.mapper.CarMapper;
import com.lukian.onlinecarsharing.model.Car;
import com.lukian.onlinecarsharing.repository.CarRepository;
import com.lukian.onlinecarsharing.service.CarService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public CarDto save(CreateCarRequestDto requestDto) {
        return carMapper.toDto(carRepository.save(carMapper.toModel(requestDto)));
    }

    @Override
    public List<CarDto> findAll(Pageable pageable) {
        return carRepository.findAll(pageable).stream()
                .map(carMapper::toDto)
                .toList();
    }

    @Override
    public CarDto finById(Long id) {
        return carMapper.toDto(getCarFromDb(id));
    }

    @Override
    public CarDto updateCarById(Long id, CreateCarRequestDto requestDto) {
        Car carFromDb = getCarFromDb(id);

        carMapper.updateModelFromDto(requestDto, carFromDb);

        return carMapper.toDto(carRepository.save(carFromDb));
    }

    @Override
    public CarDto updateCarInventoryById(Long id, UpdateCarRequestDto requestDto) {
        Car carFromDb = getCarFromDb(id);

        carFromDb.setInventory(requestDto.inventory());

        return carMapper.toDto(carRepository.save(carFromDb));
    }

    @Override
    public void deleteById(Long id) {
        carRepository.deleteById(getCarFromDb(id).getId());
    }

    private Car getCarFromDb(Long id) {
        return carRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cannot find car by id: " + id));
    }
}
