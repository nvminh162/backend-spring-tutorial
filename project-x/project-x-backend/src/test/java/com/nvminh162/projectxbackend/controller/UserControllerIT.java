package com.nvminh162.projectxbackend.controller;

// (this line not auto import invscode)
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nvminh162.projectxbackend.entity.User;
import com.nvminh162.projectxbackend.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    /*
     * @Test
     * public void createUser_shouldRReturnUser_whenValid() throws Exception {
     * // Arrange
     * User inputUser = new User(null, "nvminh162 IT", "nvminh162IT@gmail.com");
     * 
     * // Act
     * String resultStr = mockMvc.perform(
     * post("/users")
     * .contentType(MediaType.APPLICATION_JSON)
     * .content(objectMapper
     * .writeValueAsBytes(inputUser)))
     * .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString
     * ();
     * 
     * // Assert
     * System.out.println(">>>>>>>>>>>>>>>>>>>" + resultStr);
     * User outputUser = objectMapper.readValue(resultStr, User.class);
     * assertEquals(inputUser.getName(), outputUser.getName());
     * }
     */

    @Test
    public void getAllUsers() throws Exception {
        // Arrange
        this.userRepository.deleteAll();
        User user1 = new User(null, "name1", "name1@gmail.com");
        User user2 = new User(null, "name2", "name2@gmail.com");
        List<User> users = List.of(user1, user2);
        this.userRepository.saveAll(users);

        // Act
        String resultStr = this.mockMvc.perform(get("/users")).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();

        // List<User> result = this.objectMapper.readValue(resultStr, User[].class);
        List<User> result = this.objectMapper.readValue(resultStr,
                new com.fasterxml.jackson.core.type.TypeReference<List<User>>() {
                });

        // Assert
        assertEquals(2, result.size());
        assertEquals("name1", result.get(0).getName());
    }

    @Test
    public void getUserById_shouldReturnUser_whenValid() throws Exception {
        // Arrange
        this.userRepository.deleteAll();
        User user = new User(null, "name1", "name1@gmail.com");
        User userInput = this.userRepository.saveAndFlush(user);

        // Act
        String resultStr = this.mockMvc.perform(get("/users/{id}", userInput.getId())).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        User userOutput = this.objectMapper.readValue(resultStr, User.class);
        // Assert
        assertEquals("name1", userOutput.getName());
    }

    @Test
    public void getUserById_shouldEmpty_whenIdNotFound() throws Exception {
    // Arrange

    // Act
    this.mockMvc.perform(get("/users/{id}", 0)).andExpect(status().isNotFound());

    // Assert
    }

    @Test
    public void updateUser() throws Exception {
        // arrange
        this.userRepository.deleteAll();
        User user = new User(null, "old-name", "old@gmail.com");
        User userInput = this.userRepository.saveAndFlush(user);

        User updateUser = new User(userInput.getId(), "new-name", "new@gmail.com");

        // action
        String resultStr = this.mockMvc
                .perform(put("/users/{id}", userInput.getId())
                        .content(objectMapper.writeValueAsBytes(updateUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        User userOutput = this.objectMapper.readValue(resultStr, User.class);

        // Assert
        assertEquals(updateUser.getName(), userOutput.getName());
    }

    @Test
    public void deleteUser() throws Exception {
        // arrange
        this.userRepository.deleteAll();
        User user = new User(null, "delete-name", "delete@gmail.com");
        User userInput = this.userRepository.saveAndFlush(user);

        // action
        this.mockMvc.perform(delete("/users/{id}", userInput.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // assert
        long countDB = this.userRepository.count();
        assertEquals(0, countDB);

    }

    // #67
}
