package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.helpers.PostMapper;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.models.userDtos.UserUpdateDto;
import com.example.forum_management_system.services.PostService;
import com.example.forum_management_system.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
public class PostMvcController {
    private final PostService postService;
    private final PostMapper postMapper;
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;

    @Autowired
    public PostMvcController(PostService service, PostMapper postMapper, AuthenticationHelper authenticationHelper, UserService userService) {
        this.postService = service;
        this.postMapper = postMapper;
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("currentUser")
    public User currentUser(HttpSession session) {
        if (populateIsAuthenticated(session)) {
            String username = session.getAttribute("currentUser").toString();
            User user = userService.getByName(username);
            return user;
        }
        return null;
    }

    @GetMapping("/{id}")
    public String showPostPage(@PathVariable int id, Model model) {
        try {
            Post post = postService.get(id);
            model.addAttribute("post", post);
            model.addAttribute("likes", postService.showPostsLikesCount(id));
            return "Post";
        } catch (EntityNotFoundException e) {
            return "Error_Page";
        }
    }


    @PostMapping("/{id}")
    public String likeDislikePost(@PathVariable int id,
                           Model model,
                           HttpSession session) {
        Post post;
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            post = postService.get(id);

            postService.likeDislikePost(post, user);

            model.addAttribute("post", post);
            model.addAttribute("likes", postService.showPostsLikesCount(id));
            return "Post";
        } catch (EntityNotFoundException e) {
            return "Error_Page";
        }
    }
}

