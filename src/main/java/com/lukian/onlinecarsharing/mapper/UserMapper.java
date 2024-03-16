package com.lukian.onlinecarsharing.mapper;

import com.lukian.onlinecarsharing.config.MapperConfig;
import com.lukian.onlinecarsharing.dto.user.UserRegisterRequestDto;
import com.lukian.onlinecarsharing.dto.user.UserRegisterResponseDto;
import com.lukian.onlinecarsharing.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toModel(UserRegisterRequestDto requestDto);

    UserRegisterResponseDto toDto(User user);

    void updateFromDto(UserRegisterRequestDto requestDto, @MappingTarget User user);
}
