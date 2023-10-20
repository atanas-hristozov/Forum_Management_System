package com.example.forum_management_system.controllers;

import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.helpers.AuthenticationHelper;
import com.example.forum_management_system.helpers.UserMapper;
import com.example.forum_management_system.models.*;
import com.example.forum_management_system.services.CommentService;
import com.example.forum_management_system.services.PostService;
import com.example.forum_management_system.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    public static final String ERROR_MESSAGE = "You are not authorized to browse user information.";

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationHelper authenticationHelper;


    @Autowired
    public UserRestController(UserService userService,
                              UserMapper userMapper,
                              AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/{id}")
    public User get(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id, loggedUser);

            return userService.getById(id);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping
    public List<User> getAll(@RequestHeader HttpHeaders headers,
                             @RequestParam(required = false) String username,
                             @RequestParam(required = false) String email,
                             @RequestParam(required = false) String firstName) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAdmin(user);
            UserFilterOptions userFilterOptionsForAdmins = new UserFilterOptions(username,
                    email, firstName);

            return userService.getAll(userFilterOptionsForAdmins);

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public User create(@Valid @RequestBody UserCreateDto userCreateDto) {
        User user = userMapper.fromUserCreateDto(userCreateDto);
        userService.create(user);

        return user;
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User userToDelete = authenticationHelper.tryGetUser(headers);
            checkIsItSameUser(userToDelete, id);
            userService.delete(userToDelete);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public void update(@RequestHeader HttpHeaders headers,
                       @RequestBody UserUpdateDto userUpdateDto,
                       @PathVariable int id) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            checkIsItSameUser(loggedUser, id);
            User user = userMapper.fromUserUpdateDto(id, userUpdateDto);

            userService.update(user);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }@PutMapping("/admin/{id}")
    public void adminUpdate(@RequestHeader HttpHeaders headers,
                       @RequestBody AdminRightsDto adminRightsDto,
                       @PathVariable int id) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            checkAdmin(loggedUser);

            User userToUpdate = userService.getById(id);
            if (userToUpdate.isAdmin()) {
                throw new EntityDuplicateException("Admin", "id", String.valueOf(id));
            }

            userToUpdate = userMapper.fromAdminRightsDto(id, adminRightsDto);
            userService.update(userToUpdate);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    private static void checkAdmin(User userToCheck){
        if (!userToCheck.isAdmin()){
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }

    private static void checkAccessPermissions(int targetUserId, User executingUser) {
        if (!executingUser.isAdmin() && executingUser.getId() != targetUserId) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }
    private static void checkIsItSameUser(User loggedUser, int id){
        if (loggedUser.getId() != id) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }
}
