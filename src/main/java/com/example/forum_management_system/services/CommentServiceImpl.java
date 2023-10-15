package com.example.forum_management_system.services;

import com.example.forum_management_system.Models.Comment;
import com.example.forum_management_system.Models.User;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    @Override
    public Comment get(int id) {
        return null;
    }

    @Override
    public void create(Comment comment, User user) {

    }

    @Override
    public void update(Comment comment, User user) {

    }

    @Override
    public void delete(int id, User user) {

    }
}
