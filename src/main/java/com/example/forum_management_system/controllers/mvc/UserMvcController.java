package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.helpers.UserMapper;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserMvcController {
    private final UserMapper userMapper;
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;

    public UserMvcController(UserMapper userMapper, AuthenticationHelper authenticationHelper, UserService userService) {
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
    }

    /*@GetMapping
    public String showUserPage(){
        return "User";
    }*/
    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
    @GetMapping("/{id}")
    public String showUserPage(@PathVariable int id, Model model){
        try {
            User user = userService.getById(id);
            model.addAttribute("user", user);
            return "User";
        }
        catch (EntityNotFoundException e){
            return "Error_Page";
        }
    }
}
