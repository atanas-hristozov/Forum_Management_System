package com.example.forum_management_system.services;

import com.example.forum_management_system.models.Comment;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.User;

import java.util.List;
import java.util.Map;

public interface CommentService {
    List<Comment> getAllCommentsFromPost(int postId);
    List<Comment> getAll();

    Comment get(int id);
    void create(Comment comment, Post post, User user);
    void update(Comment comment,Post post, User user);
    void delete(int id, User user);
}
