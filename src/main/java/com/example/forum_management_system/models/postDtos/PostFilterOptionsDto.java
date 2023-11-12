package com.example.forum_management_system.models.postDtos;

import java.util.Optional;

public class PostFilterOptionsDto {

    private String postTitle;
    private String postAuthor;
    private String sortPostsBy;
    private String sortOrder;


    public PostFilterOptionsDto() {
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostAuthor() {
        return postAuthor;
    }

    public String getSortPostsBy() {
        return sortPostsBy;
    }


    public String getSortOrder() {
        return sortOrder;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setPostAuthor(String postAuthor) {
        this.postAuthor = postAuthor;
    }

    public void setSortPostsBy(String sortPostsBy) {
        this.sortPostsBy = sortPostsBy;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
