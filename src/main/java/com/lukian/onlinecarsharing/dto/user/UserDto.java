package com.lukian.onlinecarsharing.dto.user;

import com.lukian.onlinecarsharing.model.Role;
import java.util.Set;

public record UserDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        Set<Role> roles
) {}
