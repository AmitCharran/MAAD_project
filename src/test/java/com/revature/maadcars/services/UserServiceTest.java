package com.revature.maadcars.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.maadcars.controllers.UserController;
import com.revature.maadcars.models.User;
import com.revature.maadcars.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest {
    @MockBean
    private UserController userController;
    @MockBean
    private UserRepository userRepository;
    private UserService userService;

    private List<User> userList;
    private User user;


    @BeforeEach
    void setup(){
        userRepository = Mockito.mock(UserRepository.class);
        userController = Mockito.mock(UserController.class);
        userService = new UserService(userRepository);

        user = new User();
        user.setUser_id(1);
        user.setUsername("test_user1");
        user.setPassword("password");

        userList = new ArrayList<>();
        userList.add(user);
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
    void SaveUserToDatabseButUserPasswordTooShortOrTooLong(){
        user.setPassword("l");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        userService.saveUser(user);
        assertEquals(user, userService.saveUser(user));
    }


}