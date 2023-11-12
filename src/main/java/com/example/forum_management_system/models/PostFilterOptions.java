package com.example.forum_management_system.models;

import java.util.Optional;

public class PostFilterOptions {

    private Optional<String> postTitle;
    private Optional<String> postAuthor;
    private Optional<String> sortPostsBy;
    private Optional<String> sortOrder;


    public PostFilterOptions(String postTitle,
                             String postAuthor,
                             String sortPostsBy,
                             String sortOrder) {
        this.postTitle = Optional.ofNullable(postTitle);
        this.postAuthor = Optional.ofNullable(postAuthor);
        this.sortPostsBy = Optional.ofNullable(sortPostsBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }



    public Optional<String> getPostTitle() {
        return postTitle;
    }

    public Optional<String> getPostAuthor() {
        return postAuthor;
    }

    public Optional<String> getSortPostsBy() {
        return sortPostsBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}



