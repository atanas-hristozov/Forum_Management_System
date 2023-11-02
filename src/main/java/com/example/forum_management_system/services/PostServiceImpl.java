package com.example.forum_management_system.services;

import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.postDtos.PostDtoHome;
import com.example.forum_management_system.models.Tag;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService {
    public static final String ERROR_MESSAGE = "You are not authorized!";

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> get(String title, Integer userId, String sortBy, String sortOrder) {
        return postRepository.getAll(title, userId, sortBy, sortOrder);
    }

    @Override
    public Post get(int id) {
        return postRepository.getById(id);
    }

    @Override
    public void create(Post post, User user) {
        boolean duplicateExists = true;
        try {
            postRepository.getByTitle(post.getTitle());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new EntityDuplicateException("Post", "name", post.getTitle());
        }

        post.setCreator(user);
        postRepository.create(post);
    }

    @Override
    public void update(Post post, User user) {

        boolean duplicateExists = true;

        try {
            Post existingPost = postRepository.getByTitle(post.getTitle());
            if (existingPost.getId() == post.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Post", "title", post.getTitle());
        }
        checkAccessPermissions(post.getId(), user);
        postRepository.update(post);
    }

    public Set<Object> likeEntity(int id, User user) {
        return postRepository.likeEntity(id, user);
    }


    public Set<Object> dislikeEntity(int id, User user) {
        return postRepository.dislikeEntity(id, user);
    }

    @Override
    public int showPostsCount() {
        return postRepository.getAllCount().size();
    }

    @Override
    public List<Post> getMostRecent() {
        return postRepository.getMostRecent();
    }

    @Override
    public List<PostDtoHome> getMostCommented() {
        return postRepository.getMostCommented();
    }

    @Override
    public void delete(int postId, User user) {
        checkAccessPermissions(postId, user);
        postRepository.delete(postId);
    }

    @Override
    public Set<Tag> getTagsByPost(Post post) {
        return post.getTags();
    }

    @Override
    public void addTagToPost(Post post, Set<Tag> tagsToAdd) {
        if (tagsToAdd == null) {
            return;
        }
        if (post.getTags() == null) {
            post.setTags(new HashSet<>());
        }
        post.getTags().addAll(tagsToAdd);

        postRepository.update(post);
    }

    @Override
    public void removeTagFromPost(Post post, Set<Tag> tagsToRemove) {
        post.getTags().removeAll(tagsToRemove);
    }

    private static void checkAccessPermissions(int postId, User executingUser) {
        if (!executingUser.isAdmin() && executingUser.getId() != 1 && executingUser.getId() != postId) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }
}
