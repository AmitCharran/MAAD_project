package com.revature.maadcars.controllers;

import com.revature.maadcars.models.User;
import com.revature.maadcars.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller implementation for the User Entity.
 */
@Controller
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    /**
     * Injects service dependency
     */
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * Maps "GET users/" to return a list of all Users in database.
     * @return List<User>
     */
    @GetMapping
    public @ResponseBody
    List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    /**
     * Maps "GET users/{id}" to return the User with that user_id.
     * @param id = {id} (int)
     * @return User
     */
    @GetMapping("/{id}") // /users/9
    public @ResponseBody
    User findUserById(@PathVariable String id){
        return userService.getUserByUserId(Integer.parseInt(id));
    }

    /**
     * Maps "GET users/username/{username}" to return the User with that username.
     * @param username = {username} (String)
     * @return User
     */
    @GetMapping("/username/{username}") // /users/username/bpinkerton
    public @ResponseBody
    User findUserByUsername(@PathVariable String username){
        return userService.getUserByUsername(username);
    }

    /**
     * Maps POST Method to creation of a new persisted User based on request body.
     * @param u User object interpreted from request body.
     * @return Persisted User.
     */
    @PostMapping
    public @ResponseBody
    User createUser(@RequestBody User u){
        return userService.saveUser(u);
    }

    /**
     * Maps PUT Method to updating and persisting the User that matches the request body.
     * @param u User object interpreted from request body.
     * @return Updated User.
     */
    @PutMapping
    public @ResponseBody
    User updateUser(@RequestBody User u){
        return userService.saveUser(u);
    }

    /**
     * Maps "DELETE users/{id}" to deletion of a persisted User by their user_id.
     * @param id {id} (int)
     * @return HTTP Response Status Code OK if no terminal Exceptions thrown.
     */
    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<HttpStatus> deleteUser(@PathVariable String id){
        userService.deleteUser(Integer.parseInt(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }


}
