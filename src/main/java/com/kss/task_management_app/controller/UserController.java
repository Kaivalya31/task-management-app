package com.kss.task_management_app.controller;

import com.kss.task_management_app.model.LoginRequest;
import com.kss.task_management_app.model.User;
import com.kss.task_management_app.response.ApiResponse;
import com.kss.task_management_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>(true, "Users fetched successfully", users));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> getUsersById(@PathVariable Long userId){
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Users fetched successfully", user));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> registerUser(@RequestBody User user){
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).
                body(new ApiResponse<>(true, "User registered successfully", registeredUser));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(@RequestBody LoginRequest credentials){
        String message = userService.login(credentials.getUsername(), credentials.getPassword());
        return ResponseEntity.ok(new ApiResponse<>(true, message, null));
    }

    @DeleteMapping("/unregister/{userId}")
    public ResponseEntity<ApiResponse<Void>> unregister(@PathVariable Long userId){
        userService.unregister(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "User unregistered successfully", null));
    }

    @PutMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        userService.resetPassword(email, newPassword);
        return ResponseEntity.ok(new ApiResponse<>(true, "Password reset successfully", null));
    }

    @PutMapping("/reset-username")
    public ResponseEntity<ApiResponse<Void>> resetUsername(@RequestParam String email, @RequestParam String username) {
        userService.resetUsername(email, username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Username reset successfully", null));
    }
}
