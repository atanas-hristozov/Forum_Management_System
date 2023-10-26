package com.example.forum_management_system.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/register")
public class RegisterMcvController {
    @Controller
    @RequestMapping("/register")
    public class UserMvcController {
        @GetMapping
        public String showLoginPage(){
            return "Register";
        }
    }
}
