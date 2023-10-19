package com.example.forum_management_system.repositories;

import com.example.forum_management_system.exceptions.AlreadyDislikedException;
import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.services.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
public class PostRepositoryImpl implements PostRepository{

    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl (SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Post> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post", Post.class);
            return query.list();
        }
    }

    public Post getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, id);
            if (post == null) {
                throw new EntityNotFoundException("Post", id);
            }
            return post;
        }
    }

    public Post getByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where title = :title", Post.class);
            query.setParameter("title", title);

            List<Post> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Post", "title", title);
            }

            return result.get(0);
        }
    }

    public void create(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(post);
            session.getTransaction().commit();
        }
    }

    public void update(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
        }
    }

    public void delete(int id) {
        Post postToDelete = getById(id);
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.remove(postToDelete);
            session.getTransaction().commit();
        }
    }

    public Set<Object> likeEntity(int id, User user){
        Set<Object> result = new HashSet<Object>();
        Post post = getById(id);
        try (Session session = sessionFactory.openSession()){
            if (user.isLiked()){
                throw new AlreadyDislikedException();
            }

            post.setLikes(post.getLikes()+1);
            user.setLiked(true);

            result.add(user);
            result.add(post);
        }

        return result;
    }

    public Set<Object> dislikeEntity(int id, User user){
        Set<Object> result = new HashSet<Object>();
        Post post = getById(id);
        try (Session session = sessionFactory.openSession()){
            if (user.isDisliked()){
                throw new AlreadyDislikedException();
            }

            post.setDislikes(post.getLikes()+1);
            user.setDisliked(true);

            result.add(user);
            result.add(post);
        }

        return result;
    }
}
