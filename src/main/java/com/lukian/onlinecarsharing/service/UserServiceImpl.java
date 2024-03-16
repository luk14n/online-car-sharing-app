package com.lukian.onlinecarsharing.service;

import com.lukian.onlinecarsharing.dto.user.UserRegisterRequestDto;
import com.lukian.onlinecarsharing.dto.user.UserRegisterResponseDto;
import com.lukian.onlinecarsharing.exception.EntityNotFoundException;
import com.lukian.onlinecarsharing.exception.RegistrationException;
import com.lukian.onlinecarsharing.mapper.UserMapper;
import com.lukian.onlinecarsharing.model.Role;
import com.lukian.onlinecarsharing.model.User;
import com.lukian.onlinecarsharing.repository.RoleRepository;
import com.lukian.onlinecarsharing.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public static final String ROLE_PREFIX = "ROLE_";
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserRegisterResponseDto register(UserRegisterRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new RegistrationException("User with given email: "
                    + requestDto.email() + " already exist");
        }
        return registerNewUser(requestDto);
    }

    @Override
    public UserRegisterResponseDto updateRole(Long id, String role) {
        if (role != null) {
            User userFromDb = getUserFromDbById(id);

            userFromDb.getRoles().clear();
            userFromDb.getRoles().add(parseRole(role));
            return userMapper.toDto(userRepository.save(userFromDb));
        }
        throw new IllegalArgumentException("Role cannot be null");
    }

    @Override
    public UserRegisterResponseDto getProfileInfo(Long userId) {
        User userFromDb = getUserFromDbById(userId);
        return userMapper.toDto(userFromDb);
    }

    @Override
    public UserRegisterResponseDto updateProFileInfo(
            Long userId, UserRegisterRequestDto requestDto) {
        User userFromDb = getUserFromDbById(userId);

        userMapper.updateFromDto(requestDto, userFromDb);

        return userMapper.toDto(userRepository.save(userFromDb));
    }

    private User getUserFromDbById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cannot find user with id: " + userId));
    }

    private Role parseRole(String role) {
        String passedRole = role.toUpperCase().trim();
        Role roleFromDb = roleRepository.findRoleByRole(
                Role.RoleName.valueOf(ROLE_PREFIX + passedRole))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot find role with such name: " + role));
        return roleFromDb;
    }

    private UserRegisterResponseDto registerNewUser(UserRegisterRequestDto requestDto) {
        User user = userMapper.toModel(requestDto);

        assignInitRoles(user);

        return userMapper.toDto(userRepository.save(user));
    }

    private void assignInitRoles(User user) {
        Role initRole = roleRepository.findRoleByRole(Role.RoleName.ROLE_CUSTOMER)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot find initial role to assign by given name"));

        Set<Role> roles = user.getRoles();
        if (roles == null) {
            roles = new HashSet<>();
        }

        roles.add(initRole);
        user.setRoles(roles);
    }
}
