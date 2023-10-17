package com.example.forum_management_system.repositories;

import com.example.forum_management_system.models.Post;

import java.util.List;

public interface PostRepository {

    List<Post> getAll();
    Post getById(int id);
    Post getByTitle(String title);
    void create(Post post);
    void update(Post post);
    void delete(int id);
}
