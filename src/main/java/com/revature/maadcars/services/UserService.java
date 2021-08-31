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
     * There is also a validation function that checks if username already exists
     * or password length is between 5 and 200
     * @param user User object
     * @return Same User as input(?)
     */
    public User saveUser(User user){
        if(userIsGoodForCreation(user)){
            return userRepository.save(user);
        }else{
            return user;
        }
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

    /**
     * Takes a user Object and if the properties are valid for creation, then return true
     * Valid = between 5-200 length and username does not exists in database
     * @param u user we are checking
     * @return true or false
     */
    public boolean userIsGoodForCreation(User u){
        int passwordLength = u.getPassword().length();
        if(userRepository.findByUsername(u.getUsername()).isPresent()){
            //TODO log user already exists
            return false;
        }else if(passwordLength < 5 || passwordLength > 200){
            //TODO password length log password length
            return false;
        }else{
            //TODO password and username is good
            return true;
        }
    }
}
