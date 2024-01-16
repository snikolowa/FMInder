package com.example.fminder.integration;

import com.example.fminder.models.User;
import com.example.fminder.models.enums.Gender;
import com.example.fminder.repositories.UserRepository;
import com.example.fminder.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Objects;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
@Sql(scripts = "classpath:insert-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserServiceIntegrationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGetUserByEmail() {
        // Arrange
        createUser();

        // Act
        User result = userService.getUserByEmail("testEmail");

        // Assert
        assert Objects.equals(result.getFirstName(), "User1");
        assert Objects.equals(result.getLastName(), "LastName");
    }

    @Test
    public void updateUser(){
        // Arrange
        User currentUser = createUser();
        User userToUpdate = new User();
        userToUpdate.setEmail("testEmail333");
        userToUpdate.setLastName("newLastName");

        // Act
        User result = userService.updateUser(currentUser.getId(), userToUpdate);

        // Assert
        assert Objects.equals(result.getEmail(), "testEmail333");
        assert Objects.equals(result.getLastName(), "newLastName");
    }

    @Test
    void changePassword(){
        // Arrange
        User currentUser = createUser();
        User userToUpdatePassword = new User();
        userToUpdatePassword.setPassword("newPassword");

        // Act
        User result = userService.changePassword(currentUser.getId(), userToUpdatePassword);

        // Assert
        assert Objects.equals(result.getPassword(), "newPassword");
    }

    @Test
    void getPotentialMatches(){
        // Arrange
        User currentUser = createUser();

        // Act
        List<User> matches = userService.getPotentialMatches(currentUser.getId());

        // Assert
        assert !matches.isEmpty();
        assert matches.size() == 10;
        assert Objects.equals(matches.get(0).getFirstName(), "Jane");
        assert Objects.equals(matches.get(1).getFirstName(), "Bob");
        assert Objects.equals(matches.get(2).getFirstName(), "Michael");
        assert Objects.equals(matches.get(3).getFirstName(), "Laura");
        assert Objects.equals(matches.get(4).getFirstName(), "John");
        assert Objects.equals(matches.get(5).getFirstName(), "Alice");
        assert Objects.equals(matches.get(6).getFirstName(), "Sara");
        assert Objects.equals(matches.get(7).getFirstName(), "Chris");
        assert Objects.equals(matches.get(8).getFirstName(), "Emily");
        assert Objects.equals(matches.get(9).getFirstName(), "David");
    }

    @Test
    void getMatches(){
        // Arrange
        int userId = 1;

        // Act
        List<User> matches = userService.getMatches(userId);

        // Assert
        assert matches.size() == 1;
    }

    private User createUser(){
        User user = new User();
        user.setFirstName("User1");
        user.setLastName("LastName");
        user.setPassword("1123456");
        user.setEmail("testEmail");
        user.setGender(Gender.Male);
        return userRepository.save(user);
    }
}
