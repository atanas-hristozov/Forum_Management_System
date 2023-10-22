package com.example.forum_management_system.repositories;

import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.User;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface PostRepository {

    List<Post> getAll(String title, Integer userId, String sortBy, String sortOrder);
    List<Post> getAllCount();
    Post getById(int id);
    Post getByTitle(String title);
    void create(Post post);
    void update(Post post);
    void delete(int id);
    Set<Object> likeEntity(int id, User user);
    Set<Object> dislikeEntity(int id, User user);
}
