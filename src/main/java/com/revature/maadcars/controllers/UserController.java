package com.revature.maadcars.controllers;

import com.revature.maadcars.models.User;
import com.revature.maadcars.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public @ResponseBody
    List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}") // /users/9
    public @ResponseBody
    User findUserById(@PathVariable String id){
        return userService.getUserByUserId(Integer.parseInt(id));
    }

    @GetMapping("/username/{username}") // /users/username/bpinkerton
    public @ResponseBody
    User findUserByUsername(@PathVariable String username){
        return userService.getUserByUsername(username);
    }

    @PostMapping
    public @ResponseBody
    User createUser(@RequestBody User u){
        return userService.saveUser(u);
    }

    @PutMapping
    public @ResponseBody
    User updateUser(@RequestBody User u){
        return userService.saveUser(u);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<HttpStatus> deleteUser(@PathVariable String id){
        userService.deleteUser(Integer.parseInt(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }


}
