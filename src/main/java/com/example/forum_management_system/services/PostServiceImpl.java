package com.example.forum_management_system.services;

import com.example.forum_management_system.Models.Post;
import com.example.forum_management_system.Models.User;
import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService{

    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl (PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public List<Post> get() {
        return postRepository.getAll();
    }

    public Post get(int id) {
        return postRepository.getById(id);
    }

    public void create(Post post, User user) {
        boolean duplicateExists = true;
        try {
            postRepository.getByTitle(post.getTitle());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Beer", "name", post.getTitle());
        }

        post.setCreator(user);
        postRepository.create(post);
    }

    public void update(Post post, User user) {

        boolean duplicateExists = true;
        try {
            Post existingBeer = postRepository.getByTitle(post.getTitle());
            if (existingBeer.getId() == post.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Post", "title", post.getTitle());
        }

        postRepository.update(post);
    }

    public void delete(int id) {
        postRepository.delete(id);
    }


}
