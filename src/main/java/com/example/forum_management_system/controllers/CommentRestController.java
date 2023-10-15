package com.example.forum_management_system.controllers;

import com.example.forum_management_system.services.CommentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {
    private final CommentService service;

    public CommentRestController(CommentService service) {
        this.service = service;
    }
}
