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
        return new User(
                saved.getUserId(),
                saved.getEmail(),
                saved.getName(),
                saved.getPassword(),
                saved.getRole(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springRepo.findByEmail(email)
                .map(e -> new User(
                        e.getUserId(),
                        e.getEmail(),
                        e.getName(),
                        e.getPassword(),
                        e.getRole(),
                        e.getCreatedAt(),
                        e.getUpdatedAt()
                ));
    }

    @Override
    public List<User> findAll() {
         return springRepo.findAll().stream()
                .map(e -> new User(
                        e.getUserId(),
                        e.getEmail(),
                        e.getName(),
                        e.getPassword(),
                        e.getRole(),
                        e.getCreatedAt(),
                        e.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(String userId) {
        return springRepo.findById(userId)
                .map(e -> new User(
                        e.getUserId(),
                        e.getEmail(),
                        e.getName(),
                        e.getPassword(),
                        e.getRole(),
                        e.getCreatedAt(),
                        e.getUpdatedAt()
                ));
    }

    @Override
    public void deleteById(String userId) {
        springRepo.deleteById(userId);
    }
    
}
