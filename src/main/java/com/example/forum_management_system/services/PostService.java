package com.example.forum_management_system.services;

import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.User;


import java.util.List;
import java.util.Set;

public interface PostService {

    List<Post> get(String title, Integer userId, String sortBy, String sortOrder);

    Post get(int id);

    void create(Post post, User user);

    void update(Post post, User user);

    void delete(int id);

    Set<Object> likeEntity(int id, User user);


    Set<Object>  dislikeEntity(int id, User user);
    int showPostsCount();
}
