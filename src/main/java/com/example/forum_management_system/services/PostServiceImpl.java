package com.example.forum_management_system.services;

import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.PostFilterOptions;
import com.example.forum_management_system.models.postDtos.PostDtoHome;
import com.example.forum_management_system.models.Tag;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.repositories.PostRepository;
import com.example.forum_management_system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    public static final String ERROR_MESSAGE = "You are not authorized!";

    private final PostRepository postRepository;

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public List<Post> get(String title, Integer userId, String sortBy, String sortOrder) {
        return postRepository.getAll(title, userId, sortBy, sortOrder);
    }

    @Override
    public List<Post> getAllFromPostFilter(PostFilterOptions filterOptions) {
        return postRepository.getAllFromPostFilter(filterOptions);
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
        checkAuthor(user, post.getCreator());
        postRepository.update(post);
    }

    @Override
    public boolean isLikedBy(int postId, int userId) {
        List<User> likedUsers = postRepository.getLikedBy(postId);
        return likedUsers.contains(userService.getById(userId));
    }

    @Override
    public int showPostsLikesCount(int postId) {
        List<User> likedUsers = postRepository.getLikedBy(postId);
        return likedUsers.size();
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
    public void delete(Post post, User user) {
        checkAccessPermissions(post.getCreator(), user);
        postRepository.delete(post);
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

    @Override
    public void likeDislikePost(Post post, User user) {
        if (!post.getLikedByUsers().contains(user)) {
            post.getLikedByUsers().add(user);
            user.getLikedPosts().add(post);

        } else {
            post.getLikedByUsers().remove(user);
            user.getLikedPosts().remove(post);
        }
        postRepository.update(post);
        userRepository.update(user);
    }

    private static void checkAccessPermissions(User user, User executingUser) {
        if (!executingUser.isAdmin() && executingUser.getId() != 1 && executingUser.getId() != user.getId()) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }

    }
    private static void checkAuthor(User user, User executingUser) {
        if (executingUser.getId() != user.getId()) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }
}
