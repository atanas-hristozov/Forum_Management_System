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
    public static final String ONLY_ADMIN_OR_COMMENT_AUTHOR_CAN_DELETE_IT = "Only admin or comment author can delete it!";
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
        if (user.isBanned()) {
            throw new AuthorizationException("User is banned!");
        }
        comment.setPost_id(post);
        comment.setAuthor(user);
        repository.create(comment);
    }

    @Override
    public void update(Comment comment, Post post, User user) {
        if (!comment.getAuthor().equals(user) && !user.isAdmin() && user.isBanned()) {
            throw new AuthorizationException("User is not the author or admin or he is banned.");
        }
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

    private void checkIfUserIsAuthorOrAdmin(int commentId, User user) {
        Comment comment = repository.get(commentId);
        if ((!user.isAdmin() || comment.getAuthor().getId() != user.getId())) {
            throw new AuthorizationException(ONLY_ADMIN_OR_COMMENT_AUTHOR_CAN_DELETE_IT);
        }
    }
}
