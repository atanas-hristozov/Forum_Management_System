package com.example.forum_management_system.controllers;

import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.models.Comment;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.services.CommentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {
    private final CommentService service;
    //private final CommentMapper commentMapper;
    private final AuthenticationHelper authenticationHelper;

    public CommentRestController(CommentService service, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/{id}")
    public Comment get(@PathVariable int id){
        try{
            return service.get(id);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    /*@PostMapping("/{id}")
    public Comment create(@RequestHeader HttpHeaders headers, @Valid @RequestBody CommentDto commentDto){
        User user = authenticationHelper.tryGetUser(headers);
        Comment comment = Comm
    }*/
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
