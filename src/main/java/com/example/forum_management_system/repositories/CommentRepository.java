package com.example.forum_management_system.repositories;

import com.example.forum_management_system.models.Comment;


public interface CommentRepository {
    Comment get(int id);
    void create(Comment comment);
    void update(Comment comment);
    void delete(int id);
}
