package com.scalar.ticketing.app.springboot_crud;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.scalar.ticketing.app.springboot_crud.application.impl.UserServiceImpl;
import com.scalar.ticketing.app.springboot_crud.domain.model.User;
import com.scalar.ticketing.app.springboot_crud.domain.model.enums.UserRole;
import com.scalar.ticketing.app.springboot_crud.domain.repository.UserRepository;
import com.scalar.ticketing.app.springboot_crud.infrastructure.security.JwtUtil;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.UserRegistrationDTO;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User(
                "user-1", "test@mail.com", "Test User",
                "encodedPass", UserRole.CUSTOMER,
                LocalDateTime.now(), null
        );
    }

    @Test
    void shouldRegisterUser() {
        when(passwordEncoder.encode("secret123")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            return new User(
                    u.getUserId(), u.getEmail(), u.getName(),
                    u.getPassword(), u.getRole(),
                    LocalDateTime.now(), null
            );
        });

        UserRegistrationDTO dto = new UserRegistrationDTO(
                "Test User", "test@mail.com", "secret123", UserRole.CUSTOMER
        );

        User result = userService.registerUser(dto);

        assertNotNull(result);
        assertEquals("Test User", result.getName());
        assertEquals("test@mail.com", result.getEmail());
        assertEquals(UserRole.CUSTOMER, result.getRole());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldLoginSuccessfully() {
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("secret123", "encodedPass")).thenReturn(true);
        when(jwtUtil.generateToken("test@mail.com", "user-1", "CUSTOMER")).thenReturn("jwt-token-123");

        String token = userService.login("test@mail.com", "secret123");

        assertEquals("jwt-token-123", token);
    }

    @Test
    void shouldFailLoginWithWrongPassword() {
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("wrong", "encodedPass")).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> userService.login("test@mail.com", "wrong"));
    }

    @Test
    void shouldFailLoginWithUnknownEmail() {
        when(userRepository.findByEmail("unknown@mail.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.login("unknown@mail.com", "pass"));
    }

    @Test
    void shouldResetPassword() {
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.encode("temporal123")).thenReturn("newEncodedPass");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        assertDoesNotThrow(() -> userService.resetPassword("test@mail.com"));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldFailResetPasswordWhenUserNotFound() {
        when(userRepository.findByEmail("unknown@mail.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.resetPassword("unknown@mail.com"));
    }

    @Test
    void shouldGetUserById() {
        when(userRepository.findById("user-1")).thenReturn(Optional.of(sampleUser));

        Optional<?> result = userService.getUserById("user-1");

        assertTrue(result.isPresent());
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        when(userRepository.findById("unknown")).thenReturn(Optional.empty());

        Optional<?> result = userService.getUserById("unknown");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldGetAllUsers() {
        User user2 = new User(
                "user-2", "other@mail.com", "Other User",
                "pass2", UserRole.ADMIN,
                LocalDateTime.now(), null
        );
        when(userRepository.findAll()).thenReturn(List.of(sampleUser, user2));

        var result = userService.getAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    void shouldUpdateUser() {
        when(userRepository.findById("user-1")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.encode("newPass")).thenReturn("newEncoded");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserRegistrationDTO dto = new UserRegistrationDTO(
                "Updated Name", "updated@mail.com", "newPass", UserRole.ADMIN
        );

        var result = userService.updateUser("user-1", dto);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().name());
    }

    @Test
    void shouldDeleteUser() {
        when(userRepository.findById("user-1")).thenReturn(Optional.of(sampleUser));

        boolean deleted = userService.deleteUser("user-1");

        assertTrue(deleted);
        verify(userRepository).deleteById("user-1");
    }

    @Test
    void shouldReturnFalseWhenDeletingNonexistentUser() {
        when(userRepository.findById("unknown")).thenReturn(Optional.empty());

        boolean deleted = userService.deleteUser("unknown");

        assertFalse(deleted);
        verify(userRepository, never()).deleteById(any());
    }
}
