package com.example.forum_management_system.models;

import java.util.Optional;

public class FilterOptions {

    //filters for user criteria
    private Optional<String> username;
    private Optional<String> email;
    private Optional<String> firstName;
    private Optional<String> filterPostsByUser;

    //filters and sorting for posts
    private Optional<String> filterPostsByKeyword;
    private Optional<Boolean> sortPostsByNewest;
    private Optional<Boolean> sortPostsByMostLiked;

    public FilterOptions(String username,
                         String email,
                         String firstName,
                         String filterPostsByUser,
                         String filterPostsByKeyword,
                         Boolean sortPostsByNewest,
                         Boolean sortPostsByMostLiked) {
        this.username = Optional.ofNullable(username);
        this.email = Optional.ofNullable(email);
        this.firstName = Optional.ofNullable(firstName);
        this.filterPostsByUser = Optional.ofNullable(filterPostsByUser);
        this.filterPostsByKeyword = Optional.ofNullable(filterPostsByKeyword);
        this.sortPostsByNewest = Optional.ofNullable(sortPostsByNewest);
        this.sortPostsByMostLiked = Optional.ofNullable(sortPostsByMostLiked);
    }

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getFirstName() {
        return firstName;
    }

    public Optional<String> getFilterPostsByUser() {
        return filterPostsByUser;
    }

    public Optional<String> getFilterPostsByKeyword() {
        return filterPostsByKeyword;
    }

    public Optional<Boolean> getSortPostsByNewest() {
        return sortPostsByNewest;
    }

    public Optional<Boolean> getSortPostsByMostLiked() {
        return sortPostsByMostLiked;
    }
}
