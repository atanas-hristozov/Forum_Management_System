package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.helpers.UserMapper;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.models.UserFilterOptions;
import com.example.forum_management_system.models.userDtos.AdminRightsDto;
import com.example.forum_management_system.models.userDtos.UserFilterDto;
import com.example.forum_management_system.services.CommentService;
import com.example.forum_management_system.services.PostService;
import com.example.forum_management_system.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user/admin")
public class AdminMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    public AdminMvcController(AuthenticationHelper authenticationHelper,
                              UserService userService,
                              PostService postService,
                              CommentService commentService) {
        this.authenticationHelper = authenticationHelper;
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
    public String showAdminPage(@PathVariable int id, Model model, HttpSession session) {
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
    /*@PostMapping("/{id}")
    public String updateUserProfile(@PathVariable int id, @Valid @ModelAttribute("currentUser")AdminRightsDto adminRightsDto,
                                    BindingResult bindingResult, Model model)*/

}
