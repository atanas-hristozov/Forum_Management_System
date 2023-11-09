package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.postDtos.PostDtoHome;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.services.CommentService;
import com.example.forum_management_system.services.PostService;
import com.example.forum_management_system.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final CommentService commentService;
    @Autowired
    public HomeMvcController(PostService postService, UserService userService, CommentService commentService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
    @ModelAttribute("currentUser")
    public User currentUser(HttpSession session) {
        if (populateIsAuthenticated(session)){
            String username = session.getAttribute("currentUser").toString();
            User user = userService.getByName(username);
            return user;
        }
        return null;
    }
    @GetMapping
    public String showHomePage(Model model, HttpSession session){
        List<Post> posts = postService.get(null,null,null,null);
        List<Post> topRecentPosts = postService.getMostRecent();
        List<PostDtoHome> topCommented = postService.getMostCommented();
        int postsCount = postService.showPostsCount();
        int usersCount = userService.showUsersCount();
        model.addAttribute("posts", posts);
        model.addAttribute("topRecentPosts", topRecentPosts);
        model.addAttribute("topCommented", topCommented);
        model.addAttribute("postsCount", postsCount);
        model.addAttribute("usersCount", usersCount);
        model.addAttribute("comments", commentService);
        return "Index";
    }
}
