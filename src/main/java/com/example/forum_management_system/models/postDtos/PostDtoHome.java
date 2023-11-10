package com.example.forum_management_system.models.postDtos;

import com.example.forum_management_system.models.User;

import java.util.HashSet;
import java.util.Set;

public class PostDtoHome {
    private int id;
    private String title;
    private int commentCount;
    private String content;
    private User creator;
    private Set<User> likedByUsers;


    public PostDtoHome() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getCreator() {
        return creator;
    }
    /*
    public String getUsername(User creator){
        if(getCreator()!=null){
            return creator.getUsername();
        }
        return null;
    }*/

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Set<User> getLikedByUsers() {
        if(likedByUsers==null)
            return new HashSet<>();
        return likedByUsers;
    }

    public void setLikedByUsers(Set<User> likedByUsers) {
        this.likedByUsers = likedByUsers;
    }
}
