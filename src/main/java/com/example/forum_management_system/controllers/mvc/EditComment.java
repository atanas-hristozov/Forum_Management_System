package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.exceptions.TextLengthException;
import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.helpers.CommentMapper;
import com.example.forum_management_system.models.Comment;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.models.commentDtos.CommentDto;
import com.example.forum_management_system.services.CommentService;
import com.example.forum_management_system.services.PostService;
import com.example.forum_management_system.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts/{id}/comments/{id2}")
public class EditComment {
    private final CommentService commentService;
    private final PostService postService;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    public EditComment(CommentService commentService, PostService postService,
                       CommentMapper commentMapper, UserService userService,
                       AuthenticationHelper authenticationHelper) {
        this.commentService = commentService;
        this.postService = postService;
        this.commentMapper = commentMapper;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }


    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping()
    public String getComment(@PathVariable int id,
                             @PathVariable int id2,
                             Model model){
        Post post = postService.get(id);
        Comment comment = commentService.get(id2);
        model.addAttribute("comment", comment);
        model.addAttribute("post", post);
        return "EditComment";
    }

    @PutMapping
    public String updateComment(@PathVariable int id,
                                @PathVariable int id2,
                                @Valid @ModelAttribute("comment") CommentDto commentDto,
                                @ModelAttribute("commentModel") Comment commentModel,
                                BindingResult bindingResult,
                                Model model,
                                HttpSession httpSession) {

        User user;
        Post post;
        Comment comment;
        if (populateIsAuthenticated(httpSession)) {
            String username = httpSession.getAttribute("currentUser").toString();
            userService.getByName(username);
        }
        try {
            user = authenticationHelper.tryGetCurrentUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "Comment";
        }

        try {
            model.addAttribute("commentModel", commentModel);
            post = postService.get(id);
            comment = commentService.get(id2);
            commentService.update(comment, post, user);
            return "redirect:/" + id + "/comments/" + id2;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error_Page";
        } catch (TextLengthException e) {
            bindingResult.rejectValue("text", "invalid_length", e.getMessage());
            return "Error_Page";
        }
    }

    @PostMapping
    public String deleteComment(@PathVariable int id,
                                @PathVariable int id2,
                                @Valid @ModelAttribute("comment") CommentDto commentDto,
                                BindingResult bindingResult,
                                Model model,
                                HttpSession httpSession) {

        User user;
        Post post;
        Comment comment;
        if (populateIsAuthenticated(httpSession)) {
            String username = httpSession.getAttribute("currentUser").toString();
            userService.getByName(username);
        }
        try {
            user = authenticationHelper.tryGetCurrentUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "redirect:/posts/{id}/comments";
        }

        try {
            commentService.delete(id2, user);
            return "redirect:/posts/" + id + "/comments";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error_Page";
        }
    }
}
