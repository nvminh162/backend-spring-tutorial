package com.nvminh162.projectxbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nvminh162.projectxbackend.service.TodoService;

@RestController
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    } 

    @GetMapping("/")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok().body("HELLO WORLD");
    }

    

}
