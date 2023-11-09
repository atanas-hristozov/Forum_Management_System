package com.example.forum_management_system.repositories;

import com.example.forum_management_system.models.Comment;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommentRepositoryImpl implements CommentRepository{
    private final SessionFactory sessionFactory;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    @Autowired
    public CommentRepositoryImpl(SessionFactory sessionFactory, PostRepository postRepository, UserRepository userRepository) {
        this.sessionFactory = sessionFactory;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
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
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public User getAuthor(int authorId) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, authorId);
            if (user == null) {
                throw new EntityNotFoundException("User", authorId);
            }
            return user;
        }
    }

    @Override
    public void update(Comment comment) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Comment commentToDelete = get(id);
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.remove(commentToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Comment> getAll() {
        try(Session session = sessionFactory.openSession()){
            Query<Comment> query = session.createQuery("from Comment", Comment.class);
            return query.list();
        }
    }
    @Override
    public List<Comment> getAllCommentsFromPost(int postId){
        try(Session session = sessionFactory.openSession()){
            Post post = postRepository.getById(postId);
            Query<Comment> query = session.createQuery("from Comment WHERE post_id = :post", Comment.class);
            query.setParameter("post", post);
            List<Comment> result = query.list();
           /* if (result.isEmpty()) {
                throw new EntityNotFoundException("Comment", postId);
            }*/
            /*List<Comment> commentMap = new ArrayList<>();
            for (Comment comment: result) {
                commentMap.add(comment.getAuthor().getUsername(), comment);
            }*/
            return result;
        }
    }
    /*
    @Override
    public List<Comment> getAllCommentsFromPost(int postId){
        try(Session session = sessionFactory.openSession()){
            Post post = postRepository.getById(postId);
            Query<Comment> query = session.createQuery("from Comment WHERE post_id = :post", Comment.class);
            query.setParameter("post", post);
            List<Comment> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Comment", postId);
            }
            return result;
        }
    }*/
}
