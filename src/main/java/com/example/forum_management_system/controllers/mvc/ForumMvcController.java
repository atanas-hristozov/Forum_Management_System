package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.services.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/forum")
public class ForumMvcController {
    private final PostService service;

    public ForumMvcController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public String showForumPage(Model model){
        //model.addAttribute("posts", service.get(post))
        return "Forum";
    }
}
