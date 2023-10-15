package com.example.forum_management_system.services;

import com.example.forum_management_system.Models.Comment;
import com.example.forum_management_system.Models.User;

public interface CommentService {

    Comment get(int id);
    void create(Comment comment, User user);
    void update(Comment comment, User user);
    void delete(int id, User user);
}
