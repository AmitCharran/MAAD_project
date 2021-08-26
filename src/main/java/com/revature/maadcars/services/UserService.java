package com.revature.maadcars.services;

import com.revature.maadcars.models.User;
import com.revature.maadcars.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public User getUserByUserId(Integer id){
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void deleteUser(Integer userId){
        userRepository.findById(userId).ifPresent(userRepository::delete);
    }
}
