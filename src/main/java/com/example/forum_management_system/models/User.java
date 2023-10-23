package com.example.forum_management_system.models;

import com.example.forum_management_system.exceptions.TextLengthException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@SecondaryTable(name = "admins_phone_numbers",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id"))
public class User {

    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 32;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Post> children;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Comment> children2;

    @Column(name = "first_name")
    @Size(min = 4, max = 32, message = "First name must be between 4 and 32 symbols.")
    private String firstName;
    @Column(name = "last_name")
    @Size(min = 4, max = 32, message = "Last name must be between 4 and 32 symbols.")
    private String lastName;

    @NotNull(message = "Email can't be empty!")
    @Column(name = "email", unique = true)
    private String email;

    @NotNull(message = "Username can't be empty!")
    @Column(name = "username", unique = true)
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
    @JsonIgnore
    @Column(name = "liked")
    private boolean liked;
    @JsonIgnore
    @Column(name = "disliked")
    private boolean disliked;


    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        checkNameLength(firstName);
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        checkNameLength(lastName);
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
        if (password.isEmpty()){
            throw new EntityNotFoundException();
        }
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

    private void checkNameLength(String text) {
        if (text.length() < MIN_LENGTH || text.length() > MAX_LENGTH)
            throw new TextLengthException(MIN_LENGTH, MAX_LENGTH);
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
