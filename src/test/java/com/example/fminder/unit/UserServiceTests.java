package com.example.fminder.unit;

import com.example.fminder.exceptions.NotFoundException;
import com.example.fminder.models.User;
import com.example.fminder.repositories.RequestRepository;
import com.example.fminder.repositories.UserRepository;
import com.example.fminder.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserById_shouldReturnUser() {
        // Arrange
        int userId = 1;
        User mockUser = new User();
        when(userRepository.getUserById(userId)).thenReturn(mockUser);

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertEquals(mockUser, result);
    }

    @Test
    void getUserById_shouldThrowNotFoundException() {
        // Arrange
        int userId = 1;
        when(userRepository.getUserById(userId)).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.getUserById(userId));
    }

    // Similar tests for other methods...
    @Test
    void getUserByEmail_shouldReturnUser() {
        // Arrange
        String email = "test";
        User mockUser = new User();
        when(userRepository.getUserByEmail(email)).thenReturn(mockUser);

        // Act
        User result = userService.getUserByEmail(email);

        // Assert
        assertEquals(mockUser, result);
    }

    @Test
    void getUserByEmail_shouldThrowNotFoundException() {
        // Arrange
        String email = "test";
        when(userRepository.getUserByEmail(email)).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.getUserByEmail(email));
    }

    @Test
    void uploadProfilePicture_shouldThrowIllegalArgumentException() throws IOException {
        // Arrange
        int userId = 1;
        MultipartFile mockFile = new MockMultipartFile("test.png", new byte[]{});

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.uploadProfilePicture(mockFile, userId));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_shouldUpdateAndReturnUser() {
        // Arrange
        int userId = 1;
        User existingUser = new User();
        existingUser.setFirstName("Test1");
        existingUser.setLastName("Test1");
        existingUser.setGraduateYear(2001);
        User updatedUser = new User();
        updatedUser.setFirstName("Test2");
        updatedUser.setLastName("Test2");
        updatedUser.setGraduateYear(2002);

        when(userRepository.getUserById(userId)).thenReturn(existingUser);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Act
        User result = userService.updateUser(userId, updatedUser);

        // Assert
        assertEquals(result.getFirstName(), "Test2");
        assertEquals(result.getLastName(), "Test2");
        assertEquals(result.getGraduateYear(), 2002);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUser_shouldThrowNotFoundException() {
        // Arrange
        int userId = 1;
        User updatedUser = new User();
        when(userRepository.getUserById(userId)).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.updateUser(userId, updatedUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_shouldChangePasswordAndReturnUser() {
        // Arrange
        int userId = 1;
        User existingUser = new User();
        existingUser.setPassword("test1");
        User updatedUser = new User();
        updatedUser.setPassword("test2");
        when(userRepository.getUserById(userId)).thenReturn(existingUser);
        when(userRepository.save(existingUser)).thenReturn(updatedUser);

        // Act
        User result = userService.changePassword(userId, updatedUser);

        // Assert
        assertSame(updatedUser, result);
        assertEquals(result.getPassword(), "test2");
    }

    @Test
    void changePassword_shouldThrowNotFoundException() {
        // Arrange
        int userId = 1;
        User updatedUser = new User();
        when(userRepository.getUserById(userId)).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.changePassword(userId, updatedUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void getPotentialMatches_shouldReturnSortedMatches() {
        // Arrange
        int userId = 1;
        User currentUser = new User();
        currentUser.setGraduateYear(2023);

        User match1 = new User();
        match1.setGraduateYear(2022);

        User match2 = new User();
        match2.setGraduateYear(2024);

        List<User> potentialMatches = Arrays.asList(match1, match2);
        when(userRepository.getUserById(userId)).thenReturn(currentUser);
        when(userRepository.findAllByIdIsNot(userId)).thenReturn(potentialMatches);

        // Act
        List<User> result = userService.getPotentialMatches(userId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(match1, result.get(0));
        assertEquals(match2, result.get(1));
    }

    @Test
    void getPotentialMatches_shouldReturnEmptyList() {
        // Arrange
        int userId = 1;
        User currentUser = new User();
        when(userRepository.getUserById(userId)).thenReturn(currentUser);
        when(userRepository.findAllByIdIsNot(userId)).thenReturn(Arrays.asList());

        // Act
        List<User> result = userService.getPotentialMatches(userId);

        // Assert
        assertTrue(result.isEmpty());
    }

}
