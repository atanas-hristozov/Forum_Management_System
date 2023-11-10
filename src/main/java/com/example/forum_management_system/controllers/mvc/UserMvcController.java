package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.exceptions.TextLengthException;
import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.helpers.UserMapper;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.models.UserFilterOptions;
import com.example.forum_management_system.models.userDtos.UserFilterDto;
import com.example.forum_management_system.models.userDtos.UserUpdateDto;
import com.example.forum_management_system.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserMvcController {
    private final UserMapper userMapper;
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;

    @Autowired
    public UserMvcController(UserMapper userMapper, AuthenticationHelper authenticationHelper, UserService userService) {
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(HttpSession session) {
        if(session.getAttribute("currentUser") != null){
            Object currentUser = session.getAttribute("currentUser");
            User user = userService.getByName(currentUser.toString());
            return user.isAdmin();
        }
        return false;
    }

    @GetMapping()
    public String showUserPage(Model model, HttpSession session) {
        if (populateIsAuthenticated(session)) {
            String username = session.getAttribute("currentUser").toString();
            User user = userService.getByName(username);
            model.addAttribute("user", user);
            return "User";
        } else {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/update")
    public String showEditUserPage(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("currentUser", user);
            return "UserUpdate";
        } catch (AuthorizationException e) {

            return "redirect:/auth/login";
        }
    }


    @PostMapping("/update")
    public String updateUserProfile(@Valid @ModelAttribute("currentUser") UserUpdateDto userUpdateDto,
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
            return "UserUpdate";
        }

        try {
            user = userMapper.fromUserUpdateDto(user.getId(), userUpdateDto);
            userService.update(user);
            model.addAttribute("currentUser", user);
            return "redirect:/user";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());

            return "Error_Page";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("email", "duplicate_email", e.getMessage());

            return "UserUpdate";
        } catch (TextLengthException e) {
            bindingResult.rejectValue("firstName", "invalid_length", e.getMessage());

            return "UserUpdate";
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteUserProfile(@ModelAttribute("user") User user, HttpSession session, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/user";
        }

        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            userService.delete(user);
            session.removeAttribute("currentUser");
            return "redirect:/";

        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
}
