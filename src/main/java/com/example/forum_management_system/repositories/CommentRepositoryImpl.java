package com.example.forum_management_system.repositories;

import com.example.forum_management_system.Models.Comment;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CommentRepositoryImpl implements CommentRepository{
    private final SessionFactory sessionFactory;
    @Autowired
    public CommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Comment get(int id) {
        try (Session session = sessionFactory.openSession()){
            Comment comment = session.get(Comment.class, id);
            if(comment == null){
                throw new EntityNotFoundException("Comment", id);
            }
            return comment;
        }
    }

    @Override
    public void create(Comment comment) {

    }

    @Override
    public void update(Comment comment) {

    }

    @Override
    public void delete(Comment comment) {

    }
}
