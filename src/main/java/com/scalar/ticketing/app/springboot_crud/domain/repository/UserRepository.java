package com.scalar.ticketing.app.springboot_crud.domain.repository;

import java.util.List;
import java.util.Optional;

import com.scalar.ticketing.app.springboot_crud.domain.model.User;

public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    Optional<User> findById(String userId);
}
