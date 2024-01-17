package com.example.fminder.functional;

import com.example.fminder.FminderApplication;
import com.example.fminder.helpers.AuthenticationHelper;
import com.example.fminder.models.User;
import com.example.fminder.models.enums.Gender;
import com.example.fminder.repositories.ChatRepository;
import com.example.fminder.repositories.RequestRepository;
import com.example.fminder.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.net.URI;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FminderApplication.class)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
@Sql(scripts = "classpath:insert-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ApplicationTests {

    public static final String USERS_ENDPOINT = "/users";
    public static final String REQUESTS_ENDPOINT = "/requests";
    public static final String CHATS_ENDPOINT = "/chats";


    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    AuthenticationHelper authenticationHelper;

    @Test
    void registerUser(){
        User user = new User();
        user.setEmail("user1@abv.bg");
        user.setFirstName("TestUser");
        user.setLastName("TestUserLastName");
        user.setPassword("testPassword");
        user.setGender(Gender.Male);
        user.setGraduateYear(2020);

        HttpEntity<User> request = new HttpEntity<>(user);
        String requestUrl = "/register";
        ResponseEntity<User> response = restTemplate.exchange(requestUrl, HttpMethod.POST, request, User.class);

        assert response.getStatusCode() == HttpStatus.CREATED;
        assert Objects.equals(response.getBody().getFirstName(), "TestUser");
        assert Objects.equals(response.getBody().getLastName(), "TestUserLastName");
        assert Objects.equals(response.getBody().getEmail(), "user1@abv.bg");
        assert response.getBody().getGender() == Gender.Male;
        assert response.getBody().getGraduateYear() == 2020;
    }

//    @Test
//    void updateUserProfile() {
//        User updatedUser = new User();
//        updatedUser.setFirstName("UpdatedFirstName");
//        updatedUser.setLastName("UpdatedLastName");
//        updatedUser.setGender(Gender.Female);
//        updatedUser.setGraduateYear(2022);
//
//        MockHttpSession mockHttpSession = new MockHttpSession();
//        mockHttpSession.setAttribute("LOGGED", true);
//        mockHttpSession.setAttribute("USER_ID", 1);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Cookie", "JSESSIONID=" + mockHttpSession.getId());
//
//        HttpEntity<User> request = new HttpEntity<>(updatedUser, headers);
//        String requestUrl = "/users/profile";
//        ResponseEntity<User> response = restTemplate.exchange(requestUrl, HttpMethod.PUT, request, User.class);
//
//        assert response.getStatusCode() == HttpStatus.OK;
//        assert Objects.equals(response.getBody().getFirstName(), updatedUser.getFirstName());
//        assert Objects.equals(response.getBody().getLastName(), updatedUser.getLastName());
//        assert Objects.equals(response.getBody().getGender(), updatedUser.getGender());
//        assert Objects.equals(response.getBody().getGraduateYear(), updatedUser.getGraduateYear());
//    }


}
