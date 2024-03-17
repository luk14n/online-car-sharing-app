package com.lukian.onlinecarsharing.mapper;

import com.lukian.onlinecarsharing.config.MapperConfig;
import com.lukian.onlinecarsharing.dto.rental.RentalCreateRequestDto;
import com.lukian.onlinecarsharing.dto.rental.RentalDto;
import com.lukian.onlinecarsharing.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface RentalMapper {
    Rental toModel(RentalCreateRequestDto requestDto);

    @Mapping(target = "carId", source = "car.id")
    @Mapping(target = "userId", source = "user.id")
    RentalDto toDto(Rental rental);
}
