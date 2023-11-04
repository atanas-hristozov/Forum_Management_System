package com.example.forum_management_system.helpers;

import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.postDtos.PostDto;
import com.example.forum_management_system.services.PostService;
import com.example.forum_management_system.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    private final PostService postService;

    private final UserService userService;


    @Autowired
    public PostMapper(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    public Post fromDto(int id, PostDto dto) {
        Post post = fromDto(dto);
        post.setId(id);
        Post repositoryPost = postService.get(id);
        post.setCreator(repositoryPost.getCreator());
        post.setTags(dto.getTags());
        return post;
    }

    public Post fromDto(PostDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());


        return post;
    }
}
