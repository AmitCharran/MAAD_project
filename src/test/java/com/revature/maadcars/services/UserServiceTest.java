package com.revature.maadcars.services;

import com.revature.maadcars.controllers.UserController;
import com.revature.maadcars.models.User;
import com.revature.maadcars.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {
    @MockBean
    private UserController userController;
    @MockBean
    private UserRepository userRepository;
    private UserService userService;

    private List<User> userList;
    private User user;
    private final int TEST_USER_ID = 1;
    private final String TEST_USER_USERNAME = "test_user1";
    private final String TEST_USER_PASSWORD = "password";

    @BeforeEach
    void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        userController = Mockito.mock(UserController.class);
        userService = new UserService(userRepository);

        user = new User();
        user.setUser_id(TEST_USER_ID);
        user.setUsername(TEST_USER_USERNAME);
        user.setPassword(TEST_USER_PASSWORD);

        userList = new ArrayList<>();
        userList.add(user);
    }

    @Test
    void loginWithValidCredentials() {
        when(userRepository.findByUsername(TEST_USER_USERNAME)).thenReturn(Optional.of(user));

        User foundUser = userService.login(TEST_USER_USERNAME, TEST_USER_PASSWORD);

        assertEquals(foundUser, user);
    }

    @Test
    void loginToNonExistentUser() {
        when(userRepository.findByUsername(TEST_USER_USERNAME)).thenReturn(null);

        try{
            userService.login(TEST_USER_USERNAME, TEST_USER_PASSWORD);
            fail("Expected RuntimeException to be thrown.");
        }
        catch (RuntimeException e){
            //Do nothing because this is desired result
        }
    }

    @Test
    void loginWithIncorrectCredentials() {
        when(userRepository.findByUsername(TEST_USER_USERNAME)).thenReturn(Optional.of(user));

        try{
            userService.login(TEST_USER_USERNAME, "bazinga");
            fail("Expected RuntimeException to be thrown.");
        }
        catch (RuntimeException e){
            //Do nothing because this is desired result
        }
    }
}