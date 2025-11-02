package com.scalar.ticketing.app.springboot_crud.interfaces.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scalar.ticketing.app.springboot_crud.application.service.UserService;
import com.scalar.ticketing.app.springboot_crud.domain.repository.UserRepository;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.AuthRequestDTO;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.AuthResponseDTO;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.UserRegistrationDTO;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
     private final UserService userService;
     private final UserRepository userRepository;

    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // @PostMapping("/register")
    // public User registerUser(@RequestBody UserRegistrationDTO dto) {
    //     return userService.registerUser(dto);
    // }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDTO request) {
        userService.registerUser(request);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        String token = userService.login(request.email(), request.password());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}
