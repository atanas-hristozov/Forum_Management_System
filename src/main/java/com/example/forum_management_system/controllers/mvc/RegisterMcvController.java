package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.helpers.UserMapper;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.models.UserCreateDto;
import com.example.forum_management_system.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/register")
public class RegisterMcvController {
    private final UserService userService;
    private final UserMapper userMapper;

    public RegisterMcvController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public String showRegisterPage(Model model){
        model.addAttribute("user", new UserCreateDto());
        return "Register";
    }
    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
    @PostMapping
    public String CreateUser(@Valid @ModelAttribute("user") UserCreateDto userCreateDto,
                             Model model,
                             BindingResult result){
        if(result.hasErrors()){
            return "Register";
        }
        try {
            User user = userMapper.fromUserCreateDto(userCreateDto);
            userService.create(user);
            return "redirect:/Login";
        } catch (EntityNotFoundException e){
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityDuplicateException e){
            result.rejectValue("username", "duplicate_username", e.getMessage());
            return "Register";
        }

    }

}
