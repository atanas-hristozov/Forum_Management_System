package com.example.forum_management_system.services;

import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.postDtos.PostDtoHome;
import com.example.forum_management_system.models.Tag;
import com.example.forum_management_system.models.User;


import java.util.List;
import java.util.Set;

public interface PostService {

    List<Post> get(String title, Integer userId, String sortBy, String sortOrder);

    Post get(int id);

    void create(Post post, User user);

    void update(Post post, User user);

    void delete(int postId, User user);

   /* Set<Object> likeEntity(int id, User user);

    Set<Object>  dislikeEntity(int id, User user);*/
    int showPostsCount();
    boolean isLikedBy(int postId, int userId);
    int showPostsLikesCount(int postId);
    List<Post> getMostRecent();
    List<PostDtoHome> getMostCommented();
    Set<Tag> getTagsByPost(Post post);
    void addTagToPost(Post post, Set<Tag> tagsToAdd);
    void removeTagFromPost(Post post, Set<Tag> tagsToRemove);

    void likeDislikePost(Post post, User user);
}
