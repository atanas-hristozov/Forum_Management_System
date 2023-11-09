package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.helpers.CommentMapper;
import com.example.forum_management_system.models.Comment;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.commentDtos.CommentDto;
import com.example.forum_management_system.services.CommentService;
import com.example.forum_management_system.services.PostService;
import com.example.forum_management_system.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
