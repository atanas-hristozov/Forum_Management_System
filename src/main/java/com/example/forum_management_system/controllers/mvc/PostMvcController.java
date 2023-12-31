package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.helpers.PostMapper;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.models.postDtos.PostDto;
import com.example.forum_management_system.services.CommentService;
import com.example.forum_management_system.services.PostService;
import com.example.forum_management_system.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts/{id}")
public class PostMvcController {
    private final PostService postService;
    private final PostMapper postMapper;
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final CommentService commentService;


    @Autowired
    public PostMvcController(PostService service,
                             PostMapper postMapper,
                             AuthenticationHelper authenticationHelper,
                             UserService userService, CommentService commentService) {
        this.postService = service;
        this.postMapper = postMapper;
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.commentService = commentService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("isAuthor")
    public boolean populateIsAuthor(@PathVariable int id, HttpSession session) {
        Object currentUser = session.getAttribute("currentUser");
        if (currentUser != null) {
            String currentUsername = currentUser.toString();
            User user = userService.getByName(currentUsername);
            int currentUserId = user.getId();
            Post post = postService.get(id);
            int authorCheck = post.getCreator().getId();

            return currentUserId == authorCheck;
        }
        return false;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(HttpSession session) {
        if(session.getAttribute("currentUser") != null){
            Object currentUser = session.getAttribute("currentUser");
            User user = userService.getByName(currentUser.toString());
            return user.isAdmin();
        }
        return false;
    }


    @ModelAttribute("currentUser")
    public User currentUser(HttpSession session) {
        if (populateIsAuthenticated(session)) {
            String username = session.getAttribute("currentUser").toString();
            return userService.getByName(username);
        }
        return null;
    }

    @GetMapping
    public String showPostPage(@PathVariable int id, Model model) {
        try {
            Post post = postService.get(id);
            model.addAttribute("post", post);
            model.addAttribute("likes", postService.showPostsLikesCount(id));
            model.addAttribute("comments", commentService);

            return "Post";

        } catch (EntityNotFoundException e) {
            return "Error_Page";
        }
    }


    @PostMapping
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
            String redirectUrl = "/posts/" + post.getId();

            return "redirect:" + redirectUrl;

        } catch (EntityNotFoundException e) {
            return "Error_Page";
        }
    }

    @GetMapping("/update")
    public String showPostUpdatePage(@PathVariable int id, Model model) {
        try {
            Post post = postService.get(id);
            model.addAttribute("post", post);
            model.addAttribute("likes", postService.showPostsLikesCount(id));

            return "PostUpdate";

        } catch (EntityNotFoundException e) {
            return "Error_Page";
        }
    }

    @PostMapping("/update")
    public String updatePost(@PathVariable int id, @Valid @ModelAttribute("post") PostDto postDto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {
        User user;
        Post post;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "PostUpdate";
        }
        try {
            post = postMapper.fromDto(id, postDto);
            postService.update(post, user);
            model.addAttribute("post", post);
            String redirectUrl = "/posts/" + post.getId();

            return "redirect:" + redirectUrl;

        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());

            return "Error_Page";
        } catch (AuthorizationException e){
            model.addAttribute("errorBanned", e.getMessage());
            return "PostUpdate";
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deletePost(@PathVariable int id, HttpSession session) {
        User user;
        Post post;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {

            return "redirect:/auth/login";
        }
        post=postService.get(id);
        postService.delete(post, user);

        return "redirect:/forum";
    }
}

