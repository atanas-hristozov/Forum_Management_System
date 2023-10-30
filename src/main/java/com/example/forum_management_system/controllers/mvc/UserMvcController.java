package com.example.forum_management_system.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserMvcController {
    @GetMapping
    public String showUserPage(){
        return "User";
    }
}