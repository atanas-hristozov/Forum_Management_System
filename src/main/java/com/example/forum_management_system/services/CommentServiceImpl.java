package com.example.forum_management_system.services;


import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.models.Comment;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.repositories.CommentRepository;
import com.example.forum_management_system.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {
    public static final String ERROR_MESSAGE = "You are not authorized!";
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
    public void create(Comment comment, Post post, User user) {
        checkIfBanned(user);
        comment.setPost_id(post);
        comment.setAuthor(user);
        repository.create(comment);
    }

    @Override
    public void update(Comment comment, Post post, User user) {
        checkAuthor(comment, user);
        checkIfBanned(user);
        repository.update(comment);
    }

    @Override
    public void delete(int id, User user) {
        checkIfUserIsAuthorOrAdmin(id, user);
        repository.delete(id);
    }

    @Override
    public List<Comment> getAllCommentsFromPost(int postId) {
        return repository.getAllCommentsFromPost(postId);
    }

    @Override
    public List<Comment> getAll() {
        return null;
    }

    private void checkAuthor(Comment comment, User userToCheck){
        if (comment.getAuthor().getId() != userToCheck.getId()){
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }

    private void checkIfUserIsAuthorOrAdmin(int commentId, User user) {
        Comment comment = repository.get(commentId);
        if ((!user.isAdmin() && comment.getAuthor().getId() != user.getId())) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }
    private static void checkIfBanned(User user){
        if (user.isBanned()){
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }
}
