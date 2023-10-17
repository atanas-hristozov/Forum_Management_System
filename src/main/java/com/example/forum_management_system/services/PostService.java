package com.example.forum_management_system.services;

import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.User;


import java.util.List;

public interface PostService {

    List<Post> get();

    Post get(int id);

    void create(Post post, User user);

    void update(Post post, User user);

    void delete(int id);
}
