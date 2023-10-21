package com.example.forum_management_system.models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;

public class UserCreateDto {
    @NotNull(message = "Name can't be empty")
    @Size(min = 4, max = 32, message = "First name must be between 4 and 32 symbols.")
    private String firstName;
    @NotNull(message = "Name can't be empty")
    @Size(min = 4, max = 32, message = "Last name must be between 4 and 32 symbols.")
    private String lastName;
    @UniqueElements(message = "Email already exists!")
    @NotNull
    private String email;
    @UniqueElements(message = "Username already exists!")
    @NotNull(message = "Username can't be empty!")
    private String username;
    @NotNull(message = "Password can't be empty!")
    private String password;

    public UserCreateDto(){
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
