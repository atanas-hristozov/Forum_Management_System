package com.example.forum_management_system.models.userDtos;

import jakarta.validation.constraints.NotEmpty;

public class UserLoginDto {
    @NotEmpty(message = "Username cannot be empty.")
    private String username;
    @NotEmpty(message = "Password cannot be empty.")
    private String password;
    public UserLoginDto(){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
