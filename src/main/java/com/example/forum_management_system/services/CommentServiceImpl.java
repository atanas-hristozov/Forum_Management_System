package com.example.forum_management_system.services;


import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.models.Comment;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void create(Comment comment, User user) {
        comment.setAuthor(user);
        repository.create(comment);
    }

    @Override
    public void update(Comment comment, User user) {
        repository.update(comment);
    }

    @Override
    public void delete(int id, User user) {
        checkIfUserIsAuthorOrAdmin(id, user);
        repository.delete(id);
    }
    private void checkIfUserIsAuthorOrAdmin(int commentId, User user){
        Comment comment = repository.get(commentId);
        if (!(user.isAdmin() || comment.getAuthor().equals(user))){
            throw new AuthorizationException(ONLY_ADMIN_OR_COMMENT_AUTHOR_CAN_DELETE_IT);
        }
    }
}
