package com.nvminh162.projectxbackend.UserTest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nvminh162.projectxbackend.UserTest.User;
import com.nvminh162.projectxbackend.UserTest.UserRepository;
import com.nvminh162.projectxbackend.UserTest.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void createUser_shouldReturnUser_whenEmailValid() {
        // arrange
        User inputUser = new User(null, "nvminh162", "nvminh162@gmail.com");
        User outputUser = new User(1L, "nvminh162", "nvminh162@gmail.com");

        when(this.userRepository.existsByEmail(inputUser.getEmail())).thenReturn(false);
        when(this.userRepository.save(any())).thenReturn(outputUser);

        // act
        User result = this.userService.createUser(inputUser);

        // assert
        assertEquals(1L, result.getId());
        assertEquals("nvminh162", result.getName());
        assertEquals("nvminh162@gmail.com", result.getEmail());
    }

    @Test
    public void createUser_shouldThrowException_whenEmailInValid() {
        // arrange
        User inputUser = new User(null, "nvminh162", "nvminh162@gmail.com");

        when(this.userRepository.existsByEmail(inputUser.getEmail())).thenReturn(true);

        // act
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            this.userService.createUser(inputUser);
        });

        // assert
        assertEquals("Email already exists", ex.getMessage());
    }

    @Test
    public void getAllUsers_shouldReturnAllUsers() {
        // arrange
        List<User> outputUser = new ArrayList<>();
        outputUser.add(new User(1L, "nvminh162", "nvminh162@gmail.com"));
        outputUser.add(new User(2L, "nvminh1602", "nvminh1602@gmail.com"));

        when(this.userRepository.findAll()).thenReturn(outputUser);

        // act
        List<User> result = this.userService.getAllUsers();

        // assert
        assertEquals(2, result.size());
        assertEquals("nvminh162@gmail.com", result.get(0).getEmail());
    }

    @Test
    public void getUserById_shouldReturnOptionalUser() {
        // arrange
        Long inputId = 1L;
        User inputUser = new User(1L, "nvminh162", "nvminh162@gmail.com");

        Optional<User> userOptionalOutput = Optional.of(inputUser);

        when(this.userRepository.findById(inputId)).thenReturn(userOptionalOutput);

        // act
        Optional<User> result = this.userService.getUserById(inputId);

        // assert
        assertEquals(true, result.isPresent());
    }

    // còn delete và update
}