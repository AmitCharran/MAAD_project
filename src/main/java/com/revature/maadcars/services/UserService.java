package com.revature.maadcars.services;

import com.revature.maadcars.models.User;
import com.revature.maadcars.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
                return user.get();
            }
            else{
                throw new RuntimeException();
            }
        }
        else{
            throw new RuntimeException();
        }
    }


}