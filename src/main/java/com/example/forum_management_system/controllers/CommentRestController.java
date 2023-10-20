package com.example.forum_management_system.controllers;

import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.helpers.CommentMapper;
import com.example.forum_management_system.models.Comment;
import com.example.forum_management_system.models.CommentDto;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentRestController {
    public static final String YOU_HAVE_TO_LOG_IN_FIRST = "You have to log in first.";
    private final CommentService service;
    private final CommentMapper commentMapper;
    private final AuthenticationHelper authenticationHelper;

    public CommentRestController(CommentService service, CommentMapper commentMapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.commentMapper = commentMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<Comment> getAllFromPost(@PathVariable int postId){
        return service.getAllCommentsFromPost(postId);
    }
    @GetMapping("/{id}")
    public Comment get(@PathVariable int postId, @PathVariable int id){
        try{
            return service.get(id);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @PostMapping
    public Comment create(@RequestHeader HttpHeaders headers, @Valid @RequestBody CommentDto commentDto){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = commentMapper.fromDto(commentDto);
            service.create(comment, user);
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
    public Comment update(@RequestHeader HttpHeaders headers,@PathVariable int id, @Valid @RequestBody CommentDto commentDto){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = commentMapper.fromDto(id, commentDto);
            service.create(comment, user);
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
