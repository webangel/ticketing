package com.scalar.ticketing.app.springboot_crud.infrastructure.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.scalar.ticketing.app.springboot_crud.domain.model.User;
import com.scalar.ticketing.app.springboot_crud.domain.model.enums.UserRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @Column(name = "user_id", nullable = false, updatable = false, length = 36)
    private String userId = UUID.randomUUID().toString(); 

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "role", nullable = false)
    private UserRole role;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketEntity> tickets = new ArrayList<>();


    public User toDomain() {
        return User.builder()
                .userId(this.userId)
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .role(this.role)
                .build();
    }
       public static UserEntity fromDomain(User user) {
        return UserEntity.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword()) // Usar el enum directamente
                .role(user.getRole())
                .build();
    }
}
