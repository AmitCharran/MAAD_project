package com.revature.maadcars.services;

import com.revature.maadcars.models.User;
import com.revature.maadcars.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service-layer implementation of User Entity.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
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
     * Takes the input User object and updates that User in the database
     * @param user User object with new values
     * @return User with the new values (should be same as @param)
     */
    public User updateUser(User user){
        return userRepository.save(user);
    }

    /**
     * (Repository method call) Gets 1 User by user ID
     * @param id int
     * @return User row (only one)
     */
    public User getUserByUserId(Integer id){
        return userRepository.findById(id).orElse(null);
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
     * Looks up a user by their username and see if the input password matches their
     * stored password.
     * @param username The User's username, used to look up the User
     * @param password Input password
     * @return the User with username if the password matches their stored password
     * @throws RuntimeException if the User can't be found or the password does not match
     */
    public User login(String username, String password) throws RuntimeException{
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            //Compare input to stored password
            if(password.equals(user.get().getPassword())){
                logger.info(user.get().getUsername() + " has logged in.");
                return user.get();
            }
            else{
                logger.warn("Invalid login attempt for user " + username);
                throw new RuntimeException();
            }
        }
        else{
            logger.warn("Attempted login to nonexistent user " + username);
            throw new RuntimeException();
        }
    }

    /**
     * Takes a user Object and if the properties are valid for creation, then return true
     * Valid = between 5-200 length and username does not exist in database
     * @param u user we are checking
     * @return true or false
     */
    public boolean userIsGoodForCreation(User u){
        int passwordLength = u.getPassword().length();
        if(userRepository.findByUsername(u.getUsername()).isPresent()){
            logger.warn("Username " + u.getUsername() + " already exists");
            throw new IllegalArgumentException("Username " + u.getUsername() + " already exists");
        }else if(passwordLength < 5 || passwordLength > 200){
            if(passwordLength < 5){
                logger.warn("Password length < 5");
                throw new IllegalArgumentException("Password length cannot be less than 5");
            }else{
                logger.warn("Password length > 200");
                throw new IllegalArgumentException("Password length cannot be more than 200");
            }
        }else{
            logger.info("User with username " + u.getUsername() + " is good for creation");
            return true;
        }
    }
}


