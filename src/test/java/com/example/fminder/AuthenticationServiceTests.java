package com.example.fminder;

import com.example.fminder.exceptions.BadRequestException;
import com.example.fminder.exceptions.NotFoundException;
import com.example.fminder.models.User;
import com.example.fminder.repositories.UserRepository;
import com.example.fminder.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldRegisterAndReturnUser() {
        // Arrange
        User registerUser = new User();
        registerUser.setEmail("test@example.com");

        when(userRepository.getUserByEmail("test@example.com"))
                .thenReturn(null)
                .thenReturn(registerUser);

        when(userRepository.save(registerUser)).thenReturn(registerUser);

        // Act
        User result = authService.register(registerUser);

        // Assert
        assertNotNull(result);
        assertEquals(registerUser, result);
        verify(userRepository, times(1)).save(registerUser);
        verify(userRepository, times(2)).getUserByEmail("test@example.com");
    }

    @Test
    void register_shouldThrowBadRequestException() {
        // Arrange
        User existingUser = new User();
        existingUser.setEmail("existing@example.com");

        when(userRepository.getUserByEmail("existing@example.com")).thenReturn(existingUser);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> authService.register(existingUser));
        verify(userRepository, times(1)).getUserByEmail("existing@example.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldThrowNotFoundExceptionException() {
        // Arrange
        User registerUser = new User();
        registerUser.setEmail("test@example.com");

        when(userRepository.getUserByEmail("test@example.com")).thenReturn(null);
        when(userRepository.save(registerUser)).thenReturn(registerUser);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> authService.register(registerUser));
        verify(userRepository, times(2)).getUserByEmail("test@example.com");
        verify(userRepository, times(1)).save(registerUser);
    }

    @Test
    void login_shouldLoginAndReturnUser() {
        // Arrange
        User loginUser = new User();
        loginUser.setEmail("test@example.com");
        loginUser.setPassword("password123");

        User storedUser = new User();
        storedUser.setEmail("test@example.com");
        storedUser.setPassword("password123");

        when(userRepository.getUserByEmail("test@example.com")).thenReturn(storedUser);

        // Act
        User result = authService.login(loginUser);

        // Assert
        assertNotNull(result);
        assertEquals(storedUser, result);
        verify(userRepository, times(1)).getUserByEmail("test@example.com");
        // Add more assertions as needed for login behavior
    }

    @Test
    void login_shouldThrowNotFoundException() {
        // Arrange
        User loginUser = new User();
        loginUser.setEmail("nonexistent@example.com");

        when(userRepository.getUserByEmail("nonexistent@example.com")).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> authService.login(loginUser));
        verify(userRepository, times(1)).getUserByEmail("nonexistent@example.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_wrongCredentials_shouldThrowBadRequestException() {
        // Arrange
        User loginUser = new User();
        loginUser.setEmail("test@example.com");
        loginUser.setPassword("wrongpassword");

        User storedUser = new User();
        storedUser.setEmail("test@example.com");
        storedUser.setPassword("correctpassword");

        when(userRepository.getUserByEmail("test@example.com")).thenReturn(storedUser);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> authService.login(loginUser));
        verify(userRepository, times(1)).getUserByEmail("test@example.com");
    }

}

