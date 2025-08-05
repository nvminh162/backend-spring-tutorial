package com.nvminh162.projectxbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "todos")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private boolean isCompleted;

    public Todo(String username, boolean isCompleted) {
        this.username = username;
        this.isCompleted = isCompleted;
    }

    public Todo(Long id, String username, boolean isCompleted) {
        this.id = id;
        this.username = username;
        this.isCompleted = isCompleted;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    @Override
    public String toString() {
        return "Todo [id=" + id + ", username=" + username + ", isCompleted=" + isCompleted + "]";
    }
}
