package com.example.forum_management_system.repositories;

import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.PostFilterOptions;
import com.example.forum_management_system.models.postDtos.PostDtoHome;
import com.example.forum_management_system.models.User;

import java.util.List;
import java.util.Set;

public interface PostRepository {

    List<Post> getAllFromPostFilter(PostFilterOptions filterOptions);
    List<Post> getAll(String title, Integer userId, String sortBy, String sortOrder);
    List<Post> getAllCount();
    List<Post> getMostRecent();
    public List<PostDtoHome> getMostCommented();
    Post getById(int id);
    Post getByTitle(String title);
    void create(Post post);
    void update(Post post);
    void delete(Post post);
    List<User> getLikedBy(int postId);
}
