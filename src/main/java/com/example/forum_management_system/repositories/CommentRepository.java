package com.example.forum_management_system.repositories;

import com.example.forum_management_system.models.Comment;
import com.example.forum_management_system.models.User;

import java.util.List;
import java.util.Map;


public interface CommentRepository {
    Map<String, Comment> getAllCommentsFromPost(int postId);
    //List<Comment> getAllCommentsFromPost(int postId);
    List<Comment> getAll();
    Comment get(int id);
    void create(Comment comment);
    void update(Comment comment);
    void delete(int id);
    User getAuthor(int authorId);
}
