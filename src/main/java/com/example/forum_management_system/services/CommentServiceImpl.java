package com.example.forum_management_system.services;

import com.example.forum_management_system.models.Comment;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    @Autowired
    public CommentServiceImpl(CommentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Comment get(int id) {
        return repository.get(id);
    }

    @Override
    public void create(Comment comment, User user) {
        comment.setAuthor(user);
        repository.create(comment);
    }

    @Override
    public void update(Comment comment, User user) {
        //Fot the user we need to write a method if he is logged-in, so he can write comments
        repository.update(comment);
    }

    @Override
    public void delete(int id, User user) {
        //Fot the user we need to write a method if he is the author, so he can delete the comment
        repository.delete(id);
    }
}
