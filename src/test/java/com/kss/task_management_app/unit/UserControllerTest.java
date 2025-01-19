package com.kss.task_management_app.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kss.task_management_app.config.SecurityConfig;
import com.kss.task_management_app.controller.UserController;
import com.kss.task_management_app.model.LoginRequest;
import com.kss.task_management_app.model.User;
import com.kss.task_management_app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllUsers() throws Exception {
        // Arrange
        List<User> users = Arrays.asList(
                new User(1L, "user1", "user1@example.com", "username1", "password1", LocalDateTime.now()),
                new User(2L, "user2", "user2@example.com", "username2", "password2", LocalDateTime.now())
        );
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].username").value("username1"))
                .andExpect(jsonPath("$.data[1].username").value("username2"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUsersById_UserExists() throws Exception {
        // Arrange
        User user = new User(1L, "user1", "user1@example.com", "username1" , "password1", LocalDateTime.now());
        when(userService.getUserById(1L)).thenReturn(user);

        // Act & Assert
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("user1"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        // Arrange
        User user = new User(null, "user1", "user1@example.com", "username1", "password1", null);
        User savedUser = new User(1L, "user1", "user1@example.com", "username1", "encodedPassword", LocalDateTime.now());
        when(userService.registerUser(any(User.class))).thenReturn(savedUser);

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("username1"));

        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    void testLogin_Success() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("username1", "password1");
        when(userService.login("username1", "password1")).thenReturn("Login successful");

        // Act & Assert
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"));

        verify(userService, times(1)).login("username1", "password1");
    }

    @Test
    void testUnregister_Success() throws Exception {
        // Arrange
        when(userService.unregister(1L)).thenReturn("User unregistered successfully");

        // Act & Assert
        mockMvc.perform(delete("/api/users/unregister/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User unregistered successfully"));

        verify(userService, times(1)).unregister(1L);
    }
}
