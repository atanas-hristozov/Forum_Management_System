package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.helpers.PostMapper;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.PostDto;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.services.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/post")
public class PostMcvController {
    private final PostService postService;
    private final PostMapper postMapper;
    private final AuthenticationHelper authenticationHelper;
    @Autowired
    public PostMcvController(PostService service, PostMapper postMapper, AuthenticationHelper authenticationHelper) {
        this.postService = service;
        this.postMapper = postMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/{id}")
    public String showPostPage(@PathVariable int id, Model model){
        try {
            Post post = postService.get(id);
            model.addAttribute("post", post);
            return "Post";
        }
        catch (EntityNotFoundException e){
            return "Error_Page";
        }


    }
    @PostMapping
    public String createPost(@Valid @ModelAttribute("post")PostDto postDto, BindingResult bindingResult, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "Forum";
        }
        try {
            Post post = postMapper.fromDto(postDto);
            postService.create(post,user);

            return "redirect:/Forum";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("title", "duplicate_title", e.getMessage());
            return "Forum";
        }
    }
        /*
        @PostMapping("/new")
    public String createBeer(@Valid @ModelAttribute("beer") BeerDto beerDto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "BeerCreateView";
        }

        try {
            Beer beer = beerMapper.fromDto(beerDto);
            beerService.create(beer, user);
            return "redirect:/beers";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("name", "duplicate_beer", e.getMessage());
            return "BeerCreateView";
        }
    }
         */
}

