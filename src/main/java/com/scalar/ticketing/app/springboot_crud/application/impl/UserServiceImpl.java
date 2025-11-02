package com.scalar.ticketing.app.springboot_crud.application.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scalar.ticketing.app.springboot_crud.application.service.UserService;
import com.scalar.ticketing.app.springboot_crud.domain.model.User;
import com.scalar.ticketing.app.springboot_crud.domain.repository.UserRepository;
import com.scalar.ticketing.app.springboot_crud.infrastructure.security.JwtUtil;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.UserRegistrationDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public User registerUser(UserRegistrationDTO dto) {
        User user = User.builder()
                .userId(UUID.randomUUID().toString())
                .name(dto.name())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password())) // Aquí luego puedes encriptar
                .role(dto.role())
                .build();

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
        user.setPassword("temporal123");
        userRepository.save(user);
    }

    // private User toDomain(UserEntity entity) {
    //     return new User(entity.getUserId(), entity.getName(), entity.getEmail(), entity.getRole());
    // }

}
