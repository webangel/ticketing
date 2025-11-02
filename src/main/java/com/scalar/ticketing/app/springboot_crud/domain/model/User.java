package com.scalar.ticketing.app.springboot_crud.domain.model;

import com.scalar.ticketing.app.springboot_crud.domain.model.enums.UserRole;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class User {
    private String userId;
    private String email;
    private String name;
    private String password;
    private UserRole role;
    // private LocalDateTime createdAt;
    // private boolean active;

    
    public User(
        String userId, 
        String email, 
        String name, 
        String password, 
        UserRole role
        // LocalDateTime createdAt,
        // boolean active
        ) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        // this.createdAt = createdAt;
        // this.active = active;
    }
}
