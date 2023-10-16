package com.example.forum_management_system.controllers;

import com.example.forum_management_system.Models.Post;
import com.example.forum_management_system.Models.User;
import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private PostService postService;
    private AuthenticationHelper authenticationHelper;

    public PostRestController(PostService postService, AuthenticationHelper authenticationHelper) {
        this.postService = postService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<Post> get() {
        return postService.get();
    }

    @GetMapping("/{id}")
    public Post get(@PathVariable int id) {
        try {
            return postService.get(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Post create(@RequestHeader HttpHeaders headers, @Valid @RequestBody Post post) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            postService.create(post, user);
            return post;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Post update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody Post post) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            postService.update(post, user);
            return post;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            postService.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
