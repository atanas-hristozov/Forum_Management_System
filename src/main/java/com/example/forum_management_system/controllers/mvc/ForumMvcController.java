package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.models.Comment;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.services.CommentService;
import com.example.forum_management_system.services.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final CommentService commentService;
    @Autowired
    public ForumMvcController(PostService service, CommentService commentService) {
        this.service = service;
        this.commentService = commentService;
    }
    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showForumPage(Model model){
        List<Post> posts = service.get(null,null,null,null);
        model.addAttribute("posts", posts);
        model.addAttribute("comments", commentService);
        return "Forum";
    }

}
