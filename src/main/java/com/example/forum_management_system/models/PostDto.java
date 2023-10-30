package com.example.forum_management_system.models;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;
import java.util.Set;

public class PostDto {
    @Size(min = 16, max = 64, message = "Title length should be between 16 and 64")
    private String title;
    @Size(min = 32, max = 8192, message = "The content must be between 32 symbols and 8192 symbols.")
    private String content;
    @Positive(message = "Likes should be positive")
    private int likes;
    @Positive(message = "Dislikes should be positive")
    private int dislikes;
    @Positive(message = "UserId should be positive")
    private int userId;

    private Set<Tag> tags;



    public PostDto(){
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Set<Tag> getTags() {
        return tags;
    }
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
}
