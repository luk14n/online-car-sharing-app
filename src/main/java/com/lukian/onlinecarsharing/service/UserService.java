package com.lukian.onlinecarsharing.service;

import com.lukian.onlinecarsharing.dto.user.UserRegisterRequestDto;
import com.lukian.onlinecarsharing.dto.user.UserRegisterResponseDto;
import com.lukian.onlinecarsharing.exception.RegistrationException;

public interface UserService {
    public UserRegisterResponseDto register(UserRegisterRequestDto requestDto)
            throws RegistrationException;

    UserRegisterResponseDto updateRole(Long id, String role);

    UserRegisterResponseDto getProfileInfo(Long userId);

    UserRegisterResponseDto updateProFileInfo(Long userId, UserRegisterRequestDto requestDto);
}
