package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.exceptions.TextLengthException;
import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.helpers.UserMapper;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.models.UserFilterOptions;
import com.example.forum_management_system.models.userDtos.AdminRightsDto;
import com.example.forum_management_system.models.userDtos.AdminUpdateDto;
import com.example.forum_management_system.models.userDtos.UserFilterDto;
import com.example.forum_management_system.models.userDtos.UserUpdateDto;
import com.example.forum_management_system.services.CommentService;
import com.example.forum_management_system.services.PostService;
import com.example.forum_management_system.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user/admin")
public class AdminMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    public AdminMvcController(AuthenticationHelper authenticationHelper,
                              UserMapper userMapper,
                              UserService userService,
                              PostService postService,
                              CommentService commentService) {
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }


    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            Object currentUser = session.getAttribute("currentUser");
            User user = userService.getByName(currentUser.toString());
            return user.isAdmin();
        }
        return false;
    }

    @GetMapping
    public String showAdminPage(@ModelAttribute("userFilterOptions") UserFilterDto userFilterDto, Model model, HttpSession session) {
        if (populateIsAuthenticated(session)) {
            String username = session.getAttribute("currentUser").toString();
            User user = userService.getByName(username);
            if (user.isAdmin()) {
                UserFilterOptions filterOptions = new UserFilterOptions(
                        userFilterDto.getUsername(),
                        userFilterDto.getEmail(),
                        userFilterDto.getFirstName());
                List<User> users = userService.getAll(filterOptions);
                model.addAttribute("user", user);
                model.addAttribute("filterUsers", filterOptions);
                model.addAttribute("users", users);
                return "UserAdmin";
            }
            return "User";
        } else {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{id}")
    public String showAdminPage(@PathVariable int id,
                                Model model, HttpSession session) {
        try {
            if (populateIsAuthenticated(session)) {
                String username = session.getAttribute("currentUser").toString();
                User userToUpdate = userService.getById(id);
                User user = userService.getByName(username);
                if (user.isAdmin()) {
                    model.addAttribute("userToUpdate", userToUpdate);
                    return "UserAdminUpdate";
                }
                return "redirect:/auth/login";
            }
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        return "redirect:/auth/login";
    }
    @PostMapping("/{id}")
    public String updateUserProfile(@PathVariable int id,
                                    @Valid @ModelAttribute("userToUpdate") AdminRightsDto adminRightsDto,
                                    BindingResult bindingResult, Model model, HttpSession httpSession) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "redirect:/auth/login";
        }

        try {
           User userToUpdate = userService.getById(id);
            user = userMapper.fromAdminRightsDto(id, adminRightsDto);
            userService.update(user);
            model.addAttribute("user", user);
            String redirectUrl = "/user/admin/" + user.getId();
            return "redirect:" + redirectUrl;

        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());

            return "Error_Page";
        }
    }

    @GetMapping("/update")
    public String showEditUserPage(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("currentUser", user);
            return "AdminUpdateOwnInfo";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/update")
    public String updateAdminProfile(@Valid @ModelAttribute("currentUser") AdminUpdateDto adminUpdateDto,
                                     BindingResult bindingResult,
                                     Model model,
                                     HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "UserAdmin";
        }


        if (bindingResult.hasErrors()) {
            return "UserAdmin";
        }

        try {
            user = userMapper.fromAdminUpdateDto(user.getId(), adminUpdateDto);
            userService.update(user);
            model.addAttribute("currentUser", user);
            return "redirect:/user/admin";

        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());

            return "Error_Page";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("email", "duplicate_email", e.getMessage());

            return "AdminUpdateOwnInfo";
        } catch (TextLengthException e) {
            bindingResult.rejectValue("firstName", "invalid_length", e.getMessage());

            return "AdminUpdateOwnInfo";
        }
    }


}
