package com.nvminh162.projectxbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nvminh162.projectxbackend.entity.User;

@RestController
public class RootController {
    @Autowired
    private ObjectMapper om;

    @GetMapping("/")
    public ResponseEntity<String> index() throws Exception {
        // json => object (frontend send data to backend)//
        String json = """
                    {
                        "name": "nvminh162",
                        "email": "nvminh162@gmail.com"
                    }
                """;
        User test = om.readValue(json, User.class);
        System.out.println(test);

        // object=>json
        User nvminh162 = new User(null, "nvminh162", "nvminh162@gmail.com");
        String nvminh162Json = om.writeValueAsString(nvminh162);

        return ResponseEntity.ok().body(nvminh162Json);
    }
}
