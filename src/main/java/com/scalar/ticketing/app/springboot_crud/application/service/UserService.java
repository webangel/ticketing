package com.scalar.ticketing.app.springboot_crud.application.service;

import java.util.List;
import java.util.Optional;

import com.scalar.ticketing.app.springboot_crud.domain.model.User;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.UserRegistrationDTO;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.UserResponseDTO;

public interface UserService {
    User registerUser(UserRegistrationDTO dto);
    String login(String email, String password);
    void resetPassword(String email);
    Optional<UserResponseDTO> getUserById(String userId);
    List<UserResponseDTO> getAllUsers();
    Optional<UserResponseDTO> updateUser(String userId, UserRegistrationDTO dto);
    boolean deleteUser(String userId);
}
