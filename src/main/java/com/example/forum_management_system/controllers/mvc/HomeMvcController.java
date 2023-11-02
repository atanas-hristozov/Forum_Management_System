package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.services.PostService;
import com.example.forum_management_system.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeMvcController {
    private final PostService postService;
    private final UserService userService;

    public HomeMvcController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
    @GetMapping
    public String showHomePage(Model model){
        List<Post> posts = postService.get(null,null,null,null);
        List<Post> topRecentPosts = postService.getMostRecent();
        List<Post> topCommented = postService.getMostCommented();
        int postsCount = postService.showPostsCount();
        int usersCount = userService.showUsersCount();
        model.addAttribute("posts", posts);
        model.addAttribute("topRecentPosts", topRecentPosts);
        model.addAttribute("topCommented", topCommented);
        model.addAttribute("postsCount", postsCount);
        model.addAttribute("usersCount", usersCount);
        return "Index";
    }
}
