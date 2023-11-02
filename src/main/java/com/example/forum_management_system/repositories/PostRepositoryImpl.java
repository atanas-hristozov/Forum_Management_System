package com.example.forum_management_system.repositories;

import com.example.forum_management_system.exceptions.AlreadyDislikedException;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.models.postDtos.PostDtoHome;
import com.example.forum_management_system.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PostRepositoryImpl implements PostRepository{

    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl (SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Post> getAll(String title, Integer userId, String sortBy, String sortOrder) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post", Post.class);
            List<Post> posts = query.list();
            return filter(posts, title, userId, sortBy, sortOrder);
        }
    }

    @Override
    public List<Post> getAllCount() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post ", Post.class);
            return query.list();
        }
    }

    @Override
    public List<Post> getMostRecent() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("FROM Post ORDER BY timestamp DESC", Post.class);
            query.setMaxResults(10);
            return query.list();
        }
    }

    @Override
    public List<PostDtoHome> getMostCommented() {
        try (Session session = sessionFactory.openSession()) {
            Query<PostDtoHome[]> query = session.createQuery(
                    "SELECT p.id AS post_id, p.title AS post_title, " +
                            "p.content As post_content, COUNT(c.id) AS comment_count " +
                            "FROM Post p " +
                            "LEFT JOIN p.comments c " +
                            "GROUP BY p.id, p.title, p.content " +
                            "ORDER BY comment_count DESC, p.id"
            );
            List<PostDtoHome[]> results = query.list();

            List<PostDtoHome> postDtoList = new ArrayList<>();
            for (Object[] result : results) {
                PostDtoHome postDto = new PostDtoHome();
                postDto.setId((Integer) result[0]);
                postDto.setTitle((String) result[1]);
                postDto.setContent((String) result[2]);
                postDto.setCommentCount(((Number) result[3]).intValue());

                postDtoList.add(postDto);
            }

            return postDtoList;

         /*   try (Session session = sessionFactory.openSession()) {
                Query<Object[]> query = session.createQuery(
                        "SELECT p.id, p.title, p.content, COUNT(c.id) " +
                                "FROM Post p " +
                                "LEFT JOIN p.comments c " +
                                "GROUP BY p.id, p.title " +
                                "ORDER BY COUNT(c.id) DESC, p.id"
                );
                query.setMaxResults(10);
                List<Object[]> results = query.list();

                List<PostDtoHome> postDtoList = new ArrayList<>();
                for (Object[] result : results) {
                    PostDtoHome postDto = new PostDtoHome();
                    postDto.setId((Integer) result[0]);
                    postDto.setTitle((String) result[1]);
                    postDto.setContent((String) result[2]);
                    postDto.setCommentCount(((Number) result[3]).intValue());
                    // Handle the count as needed
                    postDtoList.add(postDto);
                }

                return postDtoList;*/
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

    public List<Post> filter(List<Post> posts, String title, Integer userId, String sortBy, String sortOrder){
        posts = filterByTitle(posts, title);
        posts = filterByUser(posts, userId);
        posts = sortBy(posts, sortBy);
        posts = order(posts, sortOrder);
        return posts;
    }

    private static List<Post> filterByTitle(List<Post> posts, String title) {
        if (title != null && !title.isEmpty()) {
            posts = posts.stream()
                    .filter(post -> containsIgnoreCase(post.getTitle(), title))
                    .collect(Collectors.toList());
        }
        return posts;
    }

    private static List<Post> filterByUser(List<Post> posts, Integer userId) {
        if (userId != null) {
            posts = posts.stream()
                    .filter(post -> post.getCreator().getId() == userId)
                    .collect(Collectors.toList());
        }
        return posts;
    }

    private static List<Post> sortBy(List<Post> posts, String sortBy) {
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy.toLowerCase()) {
                case "title":
                    posts.sort(Comparator.comparing(Post::getTitle));
                    break;
                case "user":
                    posts.sort(Comparator.comparing(post -> post.getCreator().getFirstName()));
                    break;
            }
        }
        return posts;
    }

    private static List<Post> order(List<Post> posts, String order) {
        if (order != null && !order.isEmpty()) {
            if (order.equals("desc")) {
                Collections.reverse(posts);
            }
        }
        return posts;
    }

    private static boolean containsIgnoreCase(String value, String sequence) {
        return value.toLowerCase().contains(sequence.toLowerCase());
    }
}
