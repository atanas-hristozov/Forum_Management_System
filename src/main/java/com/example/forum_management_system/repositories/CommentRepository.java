package com.example.forum_management_system.repositories;

import com.example.forum_management_system.Models.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository {
    Comment get(int id);
    void create(Comment comment);
    void update(Comment comment);
    void delete(int id);
}
