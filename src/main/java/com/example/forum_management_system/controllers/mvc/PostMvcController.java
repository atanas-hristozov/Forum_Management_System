package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.exceptions.TextLengthException;
import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.helpers.PostMapper;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.models.postDtos.PostDto;
import com.example.forum_management_system.models.userDtos.UserUpdateDto;
import com.example.forum_management_system.services.PostService;
import com.example.forum_management_system.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }
    @ModelAttribute("isAuthor")
    public boolean populateIsAuthor(HttpSession session, Post post) {
        Object currentUser = session.getAttribute("currentUser");
        Object creator = post.getCreator();

        return Objects.equals(currentUser, creator);
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

    @GetMapping("/{id}/update")
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
    @PostMapping("/{id}/update")
    public String updatePost(@PathVariable int id, @Valid @ModelAttribute("post") PostDto postDto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session){
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
            return "redirect:/forum";
        }catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());

            return "Error_Page";
        }
    }
    @RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
    public String deletePost(@ModelAttribute("post") Post post, @PathVariable int id, HttpSession session, BindingResult bindingResult) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "Forum";
        }
        postService.delete(post, user);
        return "Forum";
    }
}

