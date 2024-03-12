package com.lukian.onlinecarsharing.service;

import com.lukian.onlinecarsharing.dto.CarDto;
import com.lukian.onlinecarsharing.dto.CreateCarRequestDto;
import com.lukian.onlinecarsharing.dto.UpdateCarRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CarService {

    CarDto save(CreateCarRequestDto requestDto);

    List<CarDto> findAll(Pageable pageable);

    CarDto finById(Long id);

    CarDto updateCarById(Long id, CreateCarRequestDto requestDto);

    CarDto updateCarInventoryById(Long id, UpdateCarRequestDto requestDto);

    void deleteById(Long id);
}
