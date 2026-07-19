package com.scalar.ticketing.app.springboot_crud.application.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scalar.ticketing.app.springboot_crud.application.service.UserService;
import com.scalar.ticketing.app.springboot_crud.domain.model.User;
import com.scalar.ticketing.app.springboot_crud.domain.repository.UserRepository;
import com.scalar.ticketing.app.springboot_crud.infrastructure.security.JwtUtil;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.UserRegistrationDTO;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.UserResponseDTO;
import com.scalar.ticketing.app.springboot_crud.interfaces.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public User registerUser(UserRegistrationDTO dto) {
        User user = new User(
                UUID.randomUUID().toString(),
                dto.email(),
                dto.name(),
                passwordEncoder.encode(dto.password()),
                dto.role(),
                null,
                null
        );

        return userRepository.save(user);
    }

    @Override
    public String login(String email, String password) {
         var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

                if (!passwordEncoder.matches(password, user.getPassword())) {
                    throw new RuntimeException("Credenciales incorrectas");
                }

        return jwtUtil.generateToken(
                user.getEmail(),
                user.getUserId(),
                user.getRole().name()
        );
    }

    @Override
    public void resetPassword(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        User updatedUser = new User(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                passwordEncoder.encode("temporal123"),
                user.getRole(),
                user.getCreatedAt(),
                null
        );
        userRepository.save(updatedUser);
    }

    @Override
    public Optional<UserResponseDTO> getUserById(String userId) {
        return userRepository.findById(userId)
                .map(UserMapper::toResponse);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return UserMapper.toResponseList(userRepository.findAll());
    }

    @Override
    public Optional<UserResponseDTO> updateUser(String userId, UserRegistrationDTO dto) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                    User updatedUser = new User(
                            userId,
                            dto.email(),
                            dto.name(),
                            passwordEncoder.encode(dto.password()),
                            dto.role(),
                            existingUser.getCreatedAt(),
                            null
                    );
                    User saved = userRepository.save(updatedUser);
                    return UserMapper.toResponse(saved);
                });
    }

    @Override
    public boolean deleteUser(String userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }
}
