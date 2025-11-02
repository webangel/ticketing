package com.scalar.ticketing.app.springboot_crud.application.service;

import com.scalar.ticketing.app.springboot_crud.domain.model.User;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.UserRegistrationDTO;


public interface UserService {
    User registerUser(UserRegistrationDTO dto);
    String login(String email, String password);
    void resetPassword(String email);
     
}
