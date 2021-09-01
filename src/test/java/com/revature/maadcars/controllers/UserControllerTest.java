package com.revature.maadcars.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.maadcars.models.User;
import com.revature.maadcars.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {


    private MockMvc mockMvc;
    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    private List<User> userList;
    private User user;


    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user = new User();
        user.setUser_id(1);
        user.setUsername("test_user1");
        user.setPassword("password");

        userList = new ArrayList<>();
        userList.add(user);
    }


    @Test
    void saveUserToDatabase() throws Exception {
        when(userService.saveUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.user_id").value(1))
                .andExpect(jsonPath("$.username").value("test_user1"))
                .andExpect(jsonPath("$.password").value("password"))
                .andReturn();
    }

    @Test
    void saveUserToDatabaseHttpBadRequest() throws Exception {
        when(userService.saveUser(any(User.class))).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

}
