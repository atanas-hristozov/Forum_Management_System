package com.example.forum_management_system.controllers;

import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.services.PostService;
import com.example.forum_management_system.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
public class HomeRestController {


    private final UserService userService;
    private final PostService postService;

    public HomeRestController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping
    public int showUsersCount() {
        return userService.showUsersCount();
    }

   /* @GetMapping()
    public int showPostsCount(@RequestHeader HttpHeaders httpHeaders) {
        return postService.showUsersCount();
    }*/
}
