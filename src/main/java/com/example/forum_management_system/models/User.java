package com.example.forum_management_system.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.Objects;

@Entity
@Table(name = "users")
@SecondaryTable(name = "admins_phone_numbers",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    @Size(min = 4, max = 32, message = "First name must be between 4 and 32 symbols.")
    private String firstName;
    @Column(name = "last_name")
    @Size(min = 4, max = 32, message = "Last name must be between 4 and 32 symbols.")
    private String lastName;
    @NotNull
    @UniqueElements
    @Column(name = "email")
    private String email;
    @UniqueElements
    @NotNull(message = "Username can't be empty!")
    @Column(name = "username")
    private String username;
    @JsonIgnore
    @NotNull(message = "Password can't be empty!")
    @Column(name = "password")
    private String password;

    @Column(name = "is_admin")
    private boolean isAdmin;
    @Column(name = "is_banned")
    private boolean isBanned;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(table = "admins_phone_numbers", name = "phone_number")
    private String phoneNumber;

    @Column(name = "liked")
    private boolean liked;

    @Column(name = "disliked")
    private boolean disliked;


    public User() {
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isDisliked() {
        return disliked;
    }

    public void setDisliked(boolean disliked) {
        this.disliked = disliked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
