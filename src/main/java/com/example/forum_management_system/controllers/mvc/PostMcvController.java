package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/post")
public class PostMcvController {
    private final PostService service;
    @Autowired
    public PostMcvController(PostService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public String showPostPage(@PathVariable int id, Model model){
        try {
            Post post = service.get(id);
            model.addAttribute("post", post);
            return "Post";
        }
        catch (EntityNotFoundException e){
            return "Error_Page";
        }
    }
}

