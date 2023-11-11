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
@RequestMapping("/posts/{postId}/comments/{commentId}")
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

    @ModelAttribute("isAuthor")
    public boolean populateIsAuthor(@PathVariable int postId, @PathVariable int commentId, HttpSession session) {
        Object currentUser = session.getAttribute("currentUser");
        Post post = postService.get(postId);
        if (currentUser != null) {
            String currentUsername = currentUser.toString();
            User user = userService.getByName(currentUsername);
            Comment comment = commentService.get(commentId);
            return comment.getAuthor().getId() == user.getId();
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

    @GetMapping()
    public String getComment(@PathVariable int postId,
                             @PathVariable int commentId,
                             Model model){
        Post post = postService.get(postId);
        Comment comment = commentService.get(commentId);
        model.addAttribute("comment", comment);
        model.addAttribute("post", post);
        return "CommentOptions";
    }
    @GetMapping("/update")
    public String getCommentUpdate(@PathVariable int postId,
                             @PathVariable int commentId,
                             Model model){
        Post post = postService.get(postId);
        Comment comment = commentService.get(commentId);
        model.addAttribute("comment", comment);
        model.addAttribute("post", post);
        return "EditComment";
    }

    @PostMapping("/update")
    public String updateComment(@PathVariable int postId,
                                @PathVariable int commentId,
                                @Valid @ModelAttribute("comment") CommentDto commentDto,
                                BindingResult bindingResult,
                                Model model,
                                HttpSession httpSession) {

        User user;
        Post post;
        Comment comment;
        try {
            user = authenticationHelper.tryGetCurrentUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "EditComment";
        }
        try {

            post = postService.get(postId);
            comment = commentMapper.fromDto(commentId, commentDto);
            commentService.update(comment, post, user);

            String redirectUrl = "/posts/" + post.getId() + "/comments/" + comment.getId();
            return "redirect:" + redirectUrl;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error_Page";
        } catch (TextLengthException e) {
            bindingResult.rejectValue("text", "invalid_length", e.getMessage());
            return "Error_Page";
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteComment(@PathVariable int postId,
                                @PathVariable int commentId,
                                Model model,
                                HttpSession httpSession) {

        User user;
        if (populateIsAuthenticated(httpSession)) {
            String username = httpSession.getAttribute("currentUser").toString();
            userService.getByName(username);
        }
        try {
            user = authenticationHelper.tryGetCurrentUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }


        try {
            commentService.delete(commentId, user);
            return "redirect:/posts/" + postId + "/comments";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error_Page";
        }
    }
}
