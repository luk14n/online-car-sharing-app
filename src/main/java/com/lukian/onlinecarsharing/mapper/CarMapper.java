package com.lukian.onlinecarsharing.mapper;

import com.lukian.onlinecarsharing.config.MapperConfig;
import com.lukian.onlinecarsharing.dto.car.CarDto;
import com.lukian.onlinecarsharing.dto.car.CreateCarRequestDto;
import com.lukian.onlinecarsharing.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CarMapper {

    public CarDto toDto(Car car);

    Car toModel(CreateCarRequestDto requestDto);

    void updateModelFromDto(CreateCarRequestDto requestDto,
                            @MappingTarget Car carFromDb);
}
