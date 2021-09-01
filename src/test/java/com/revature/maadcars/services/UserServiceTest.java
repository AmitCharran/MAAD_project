package com.revature.maadcars.services;

import com.revature.maadcars.models.User;
import com.revature.maadcars.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserServiceTest {
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
        userService = new UserService(userRepository);

        user = new User();
        user.setUser_id(1);
        user.setUsername("test_user1");
        user.setPassword("password");

        userList = new ArrayList<>();
        userList.add(user);
    }
  
    @Test
    void loginWithValidCredentials() {
        when(userRepository.findByUsername("test_user1")).thenReturn(Optional.of(user));

        User foundUser = userService.login("test_user1", "password");

        assertEquals(foundUser, user);
    }

    @Test
    void loginToNonExistentUser() {
        when(userRepository.findByUsername("test_user1")).thenReturn(null);

        try{
            userService.login("test_user1", "password");
            fail("Expected RuntimeException to be thrown.");
        }
        catch (RuntimeException e){
            //Do nothing because this is desired result
        }
    }

    @Test
    void loginWithIncorrectCredentials() {
        when(userRepository.findByUsername("test_user1")).thenReturn(Optional.of(user));

        try{
            userService.login("test_user1", "bazinga");
            fail("Expected RuntimeException to be thrown.");
        }
        catch (RuntimeException e){
            //Do nothing because this is desired result
        }
    }

    @Test
    void saveUserToDatabase() throws Exception {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        userService.saveUser(user);
        assertEquals(user, userService.saveUser(user));

    }

    @Test
    void SaveUserToDatabaseButUserNameAlreadyTaken() throws Exception{
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        userService.saveUser(user);
        assertEquals(user, userService.saveUser(user));
    }

    @Test
    void SaveUserToDatabaseButUserPasswordTooShortOrTooLong(){
        user.setPassword("l");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        userService.saveUser(user);
        assertEquals(user, userService.saveUser(user));
    }
}