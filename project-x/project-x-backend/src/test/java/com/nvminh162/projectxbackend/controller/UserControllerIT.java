package com.nvminh162.projectxbackend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nvminh162.projectxbackend.entity.User;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createUser_shouldRReturnUser_whenValid() throws Exception {
        // Arrange
        User inputUser = new User(null, "nvminh162 IT", "nvminh162IT@gmail.com");

        // Act
        /*
         * (this line not auto import invscode
         * => import static
         * org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;)
         */
        String resultStr = mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsBytes(inputUser)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        // Assert
        System.out.println(">>>>>>>>>>>>>>>>>>>" + resultStr);
        User outputUser = objectMapper.readValue(resultStr, User.class);
        assertEquals(inputUser.getName(), outputUser.getName());
    }
}
