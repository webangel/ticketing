package com.scalar.ticketing.app.springboot_crud.domain.model;

import java.time.LocalDateTime;

import com.scalar.ticketing.app.springboot_crud.domain.model.enums.UserRole;

public class User {

    private final String userId;
    private final String email;
    private final String name;
    private final String password;
    private final UserRole role;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public User(String userId, String email, String name, String password, UserRole role,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public UserRole getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
