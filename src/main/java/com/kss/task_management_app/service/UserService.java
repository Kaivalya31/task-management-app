package com.kss.task_management_app.service;

import com.kss.task_management_app.exception.InvalidCredentialsException;
import com.kss.task_management_app.exception.UserAlreadyExistsException;
import com.kss.task_management_app.exception.UserNotFoundException;
import com.kss.task_management_app.model.User;
import com.kss.task_management_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }

    public User registerUser(User user) {
        if(userRepository.findByUsername(user.getUsername()) != null){
            throw new UserAlreadyExistsException("Username already exists: " + user.getUsername());
        }

        if(userRepository.findByEmail(user.getEmail()) != null){
            throw new UserAlreadyExistsException("Email already exists: " + user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if(user == null || !passwordEncoder.matches(password, user.getPassword())){
            throw new InvalidCredentialsException("Invalid username or password");
        }

        return "Login successful";
    }

    public String unregister(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        userRepository.delete(user);
        return "User unregistered successfully";
    }

    public String resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("User with email " + email + " not found");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "Password reset successfully";
    }

    public String resetUsername(String email, String username) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("User with email " + email + " not found");
        }

        user.setUsername(username);
        userRepository.save(user);
        return "Username reset successfully";
    }
}
