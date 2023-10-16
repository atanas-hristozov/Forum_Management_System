package com.example.forum_management_system.services;

import com.example.forum_management_system.Models.Post;
import com.example.forum_management_system.Models.User;
import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;

import java.util.List;

public interface PostService {

    List<Post> get();

    Post get(int id);

    void create(Post post, User user);

    void update(Post post, User user);

    void delete(int id);
}
