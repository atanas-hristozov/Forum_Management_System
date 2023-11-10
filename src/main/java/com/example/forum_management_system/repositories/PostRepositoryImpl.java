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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory) {
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
                            "p.content As post_content, p.creator as post_creator, " +
                            "COUNT(c.id) AS comment_count, size (p.likedByUsers) " +
                            "FROM Post p " +
                            "LEFT JOIN p.creator u " +
                            "LEFT JOIN p.comments c " +
                            "GROUP BY p.id, p.title, p.content " +
                            "ORDER BY comment_count DESC, p.id"
            );
            query.setMaxResults(10);
            List<PostDtoHome[]> results = query.list();

            List<PostDtoHome> postDtoList = new ArrayList<>();
            for (Object[] result : results) {
                PostDtoHome postDto = new PostDtoHome();
                postDto.setId((Integer) result[0]);
                postDto.setTitle((String) result[1]);
                postDto.setContent((String) result[2]);
                postDto.setCreator((User) result[3]);
                postDto.setCommentCount(((Number) result[4]).intValue());
                postDto.setLikesCount(((Number) result[5]).intValue());

                postDtoList.add(postDto);
            }

            return postDtoList;
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
            if (result.isEmpty()) {
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

    public void delete(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> getLikedBy(int postId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Integer> query = session.
                    createQuery("SELECT u.id FROM Post p JOIN p.likedByUsers u WHERE p.id = :postId",
                            Integer.class);
            query.setParameter("postId", postId);
            List<Integer> likedUserIds = query.list();

            List<User> likedUsers = new ArrayList<>();
            for (Integer userId : likedUserIds) {
                User user = session.get(User.class, userId);
                if (user != null) {
                    likedUsers.add(user);
                }
            }
            return likedUsers;
        }
    }

    public List<Post> filter(List<Post> posts, String title, Integer userId, String sortBy, String sortOrder) {
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
