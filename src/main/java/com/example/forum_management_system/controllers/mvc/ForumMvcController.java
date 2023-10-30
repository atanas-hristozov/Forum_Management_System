package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.PostDto;
import com.example.forum_management_system.services.PostService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/forum")
public class ForumMvcController {
    private final PostService service;

    public ForumMvcController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public String showForumPage(Model model){
        List<Post> posts = service.get(null,null,null,null);
        model.addAttribute("posts", posts);
        return "Forum";
    }

    /*@GetMapping
    public String createPost(@Valid @ModelAttribute PostDto post){
        //service.create(post);
        return "redirect:Form";
    }*/
}