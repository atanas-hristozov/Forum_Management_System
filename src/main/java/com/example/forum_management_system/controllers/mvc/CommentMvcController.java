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

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/posts/{id}/comments")
public class CommentMvcController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final AuthenticationHelper authenticationHelper;
    private final PostService postService;
    private final UserService userService;

    public CommentMvcController(CommentService commentService,
                                CommentMapper commentMapper,
                                AuthenticationHelper authenticationHelper,
                                PostService postService,
                                UserService userService) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.authenticationHelper = authenticationHelper;
        this.postService = postService;
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
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


    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping()
    public String showPostCommentsPage(@PathVariable int id, Model model) {
        Post post = postService.get(id);
        List<Comment> comments = commentService.getAllCommentsFromPost(id);
        model.addAttribute("comments", comments);
        model.addAttribute("post", post);
        model.addAttribute("likes", postService.showPostsLikesCount(id));
        model.addAttribute("comment", new CommentDto());
        model.addAttribute("allComments", commentService);

        return "Comment";
    }

    @PostMapping()
    public String createNewComment(@PathVariable int id,
                                   @Valid @ModelAttribute("comment") CommentDto commentDto,
                                   BindingResult bindingResult,
                                   Model model,
                                   HttpSession httpSession) {

        User user;
        Comment comment;
        Post post;
        if (populateIsAuthenticated(httpSession)) {
            String username = httpSession.getAttribute("currentUser").toString();
            user = userService.getByName(username);
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
            post = postService.get(id);
            comment = commentMapper.fromDto(commentDto);
            commentService.create(comment, post, user);
            model.addAttribute("user", user);
            model.addAttribute("post", post);
            model.addAttribute("comment", commentDto);

            String redirectUrl = "/posts/" + post.getId() + "/comments";
            return "redirect:" + redirectUrl;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error_Page";
        }
    }
}
