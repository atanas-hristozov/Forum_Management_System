package com.example.forum_management_system.services;

import com.example.forum_management_system.models.Comment;
import com.example.forum_management_system.models.User;

public interface CommentService {

    Comment get(int id);
    void create(Comment comment, User user);
    void update(Comment comment, User user);
    void delete(int id, User user);
}
