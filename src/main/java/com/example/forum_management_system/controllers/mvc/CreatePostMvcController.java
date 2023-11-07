package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.exceptions.TextLengthException;
import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.helpers.PostMapper;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.postDtos.PostDto;
import com.example.forum_management_system.models.User;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts/new")
public class CreatePostMvcController {
    private final PostService postService;
    private final AuthenticationHelper authenticationHelper;
    private final PostMapper postMapper;
    private final UserService userService;
    @Autowired
    public CreatePostMvcController(PostService postService,
                                   AuthenticationHelper authenticationHelper,
                                   PostMapper postMapper,
                                   UserService userService) {
        this.postService = postService;
        this.authenticationHelper = authenticationHelper;
        this.postMapper = postMapper;
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

    @GetMapping
    public String showCreatePage(Model model){
        model.addAttribute("post", new PostDto());
        return "CreateNewPost";
    }

    @PostMapping
    public String createPost(@Valid @ModelAttribute("post") PostDto postDto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {

        if (populateIsAuthenticated(session)){
            String username = session.getAttribute("currentUser").toString();
            User user = userService.getByName(username);
        }
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "CreateNewPost";
        }
        try {
            Post post = postMapper.fromDto(postDto);
            postService.create(post,user);
            model.addAttribute("post", post);
            return "redirect:/forum";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error_Page";
        }

    }
}
