package com.scalar.ticketing.app.springboot_crud.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.scalar.ticketing.app.springboot_crud.domain.model.User;
import com.scalar.ticketing.app.springboot_crud.domain.repository.UserRepository;
import com.scalar.ticketing.app.springboot_crud.infrastructure.entity.UserEntity;

import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository{
    
    private final SpringDataUserRepository springRepo;
    
    @Override
    public User save(User user) {
        var entity = new UserEntity();
        entity.setUserId(user.getUserId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole());

        var saved = springRepo.save(entity);
        return User.builder()
                .userId(saved.getUserId())
                .name(saved.getName())
                .email(saved.getEmail())
                .password(saved.getPassword())
                .role(saved.getRole())
                .build();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springRepo.findByEmail(email)
                .map(e -> User.builder()
                        .userId(e.getUserId())
                        .name(e.getName())
                        .email(e.getEmail())
                        .password(e.getPassword())
                        .role(e.getRole())
                        .build());
    }

    @Override
    public List<User> findAll() {
         return springRepo.findAll().stream()
                .map(e -> User.builder()
                        .userId(e.getUserId())
                        .name(e.getName())
                        .email(e.getEmail())
                        .password(e.getPassword())
                        .role(e.getRole())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(String userId) {
         try {
             return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error saving ticket", e);
        }
    }
    
}
