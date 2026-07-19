package com.scalar.ticketing.app.springboot_crud.interfaces.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.scalar.ticketing.app.springboot_crud.domain.model.User;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.UserResponseDTO;

public class UserMapper {

    private UserMapper() {}

    public static UserResponseDTO toResponse(User user) {
        if (user == null) return null;
        return new UserResponseDTO(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public static List<UserResponseDTO> toResponseList(List<User> users) {
        return users.stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }
}
