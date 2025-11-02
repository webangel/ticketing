package com.scalar.ticketing.app.springboot_crud.interfaces.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scalar.ticketing.app.springboot_crud.application.service.UserService;
import com.scalar.ticketing.app.springboot_crud.domain.model.User;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.UserRegistrationDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
     private final UserService userService;

    // @PostMapping("/register")
    // public User registerUser(@RequestBody UserRegistrationDTO dto) {
    //     return userService.registerUser(dto);
    // }

    // @PostMapping("/login")
    // public User login(@RequestParam String email, @RequestParam String password) {
    //     return userService.login(email, password);
    // }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email) {
        userService.resetPassword(email);
        return "Contraseña restablecida para " + email;
    }

    // @GetMapping
    // public ResponseEntity<List<UserResponseDTO>> getAll() {
    //     List<UserResponseDTO> events = userService.getAll();
    //     return ResponseEntity.ok(events);
    // }
}
