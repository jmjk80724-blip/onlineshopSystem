package com.ecommerce.onlineshopsystem.user;

import com.ecommerce.onlineshopsystem.user.dto.RegisterRequest;
import com.ecommerce.onlineshopsystem.user.dto.LoginRequest;
import com.ecommerce.onlineshopsystem.user.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterRequest registerRequest;
    private User existingUser;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("john123");
        registerRequest.setPassword("password123");
        registerRequest.setEmail("john@example.com");
        registerRequest.setFullName("John Doe");
        registerRequest.setPhone("012345678");

        existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("john123");
        existingUser.setPassword("hashedPassword");
        existingUser.setEmail("john@example.com");
        existingUser.setRole("customer");
    }

    // ---------- REGISTER TESTS ----------

    @Test
    void register_ShouldSucceed_WhenUsernameAndEmailAreUnique() {
        when(userRepository.existsByUsername("john123")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserResponse response = userService.register(registerRequest);

        assertNotNull(response);
        assertEquals("john123", response.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }
    @Test
    void register_ShouldThrowException_WhenUsernameAIreadyExist() {
        when(userRepository.existsByUsername("john123")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(registerRequest)
        );
        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExist() {
        when(userRepository.existsByUsername("john123")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);
        assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(registerRequest)
        );
    }

    @Test
    void login_ShouldSucced_WhenCredentialsAreValid() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("john123");
        loginRequest.setPassword("password123");

        when(userRepository.findByUsername("john123")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);

        UserResponse response = userService.login(loginRequest);

        assertNotNull(response);
        assertEquals("john123", response.getUsername());

    }
    @Test
    void login_ShouldThrowException_WhenPasswordIsIncorrect() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("john123");
        loginRequest.setPassword("wrongPassword");

        when(userRepository.findByUsername("john123")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.login(loginRequest));
    }
    @Test
    void getUserById_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        UserResponse response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }
    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(999L));
    }

    }
