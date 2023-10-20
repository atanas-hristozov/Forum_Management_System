package com.example.forum_management_system.services;

import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> get() {
        return postRepository.getAll();
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

    @Override
    public void delete(int id) {
        postRepository.delete(id);
    }

    public Set<Object>  likeEntity(int id, User user){
        return postRepository.likeEntity(id,user);
    }


    public Set<Object> dislikeEntity(int id, User user){
        return postRepository.dislikeEntity(id,user);
    }

    @Override
    public int showPostsCount() {
        return postRepository.getAll().size();
    }


}
