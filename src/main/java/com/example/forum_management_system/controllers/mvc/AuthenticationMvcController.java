package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.helpers.UserMapper;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.models.userDtos.UserLoginDto;
import com.example.forum_management_system.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {
    private final AuthenticationHelper authenticationHelper;
    @Autowired
    public AuthenticationMvcController(AuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model){
        model.addAttribute("login", new UserLoginDto());
        return "Login";
    }
    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") UserLoginDto userLoginDto,
                              BindingResult bindingResult,
                              HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "Login";
        }

        try {
            authenticationHelper.verifyAuthentication(userLoginDto.getUsername(), userLoginDto.getPassword());
            session.setAttribute("currentUser", userLoginDto.getUsername());
            return "redirect:/";
        } catch (AuthorizationException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            return "Login";
        }
    }

    @PostMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/";
    }

}
