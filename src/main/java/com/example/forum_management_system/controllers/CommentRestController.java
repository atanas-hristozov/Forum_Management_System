package com.example.forum_management_system.controllers;

import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.helpers.CommentMapper;
import com.example.forum_management_system.models.Comment;
import com.example.forum_management_system.models.commentDtos.CommentDto;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.services.CommentService;
import com.example.forum_management_system.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentRestController {
    public static final String YOU_HAVE_TO_LOG_IN_FIRST = "You have to log in first.";
    private final CommentService service;
    private final CommentMapper commentMapper;
    private final PostService postService;
    private final AuthenticationHelper authenticationHelper;

    public CommentRestController(CommentService service, CommentMapper commentMapper, PostService postService, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.commentMapper = commentMapper;
        this.postService = postService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public Map<String, Comment> getAllFromPost(@PathVariable int postId){
        return service.getAllCommentsFromPost(postId);
    }
    @GetMapping("/{id}")
    public Comment get(@PathVariable int id){
        try{
            return service.get(id);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @PostMapping
    public Comment create(@RequestHeader HttpHeaders headers, @PathVariable int postId, @Valid @RequestBody CommentDto commentDto){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postService.get(postId);
            Comment comment = commentMapper.fromDto(commentDto);
            service.create(comment, post, user);
            return comment;
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Comment update(@RequestHeader HttpHeaders headers,@PathVariable int postId, @PathVariable int id, @Valid @RequestBody CommentDto commentDto){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postService.get(postId);
            Comment comment = commentMapper.fromDto(id, commentDto);
            service.update(comment,post, user);
            return comment;
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.delete(id, user);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
