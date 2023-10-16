package com.example.forum_management_system.controllers;

import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.services.UserService;

public class UserRestController {


    private  final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }


}
