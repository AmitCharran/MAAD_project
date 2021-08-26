package com.revature.maadcars.services;

import com.revature.maadcars.models.User;
import com.revature.maadcars.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service-layer implementation of User Entity.
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    /**
     * Injects repository dependency
     */
    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * (Repository method call) Gets 1 User by username
     * @param username String
     * @return User row (only one)
     */
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
    }

    /**
     * (Repository method call) Persists input User into 1 row
     * @param user User object
     * @return Same User as input(?)
     */
    public User saveUser(User user){
        return userRepository.save(user);
    }

    /**
     * (Repository method call) Gets 1 User by user ID
     * @param id int
     * @return User row (only one)
     */
    public User getUserByUserId(Integer id){
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    /**
     * (Repository method call) Gets List of all Users
     * @return List<User>
     */

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    /**
     * (Repository method call) Deletes row that corresponds to input User if present
     * @param userId int
     */
    public void deleteUser(Integer userId){
        userRepository.findById(userId).ifPresent(userRepository::delete);
    }
}