package com.example.forum_management_system.services;

import com.example.forum_management_system.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl {

    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl (PostRepository postRepository){
        this.postRepository = postRepository;
    }


}
