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


    @Autowired
    public PostMapper(PostService postService) {
        this.postService = postService;

    }

    public Post fromDto(int id, PostDto dto) {
        Post post =  postService.get(id);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
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
