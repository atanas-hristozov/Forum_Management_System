package com.example.forum_management_system.services;

import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.models.UserFilterOptions;
import com.example.forum_management_system.repositories.CommentRepository;
import com.example.forum_management_system.repositories.PostRepository;
import com.example.forum_management_system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication.";
    private final UserRepository userRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> getAll(UserFilterOptions userFilterOptions) {
        return userRepository.getAll(userFilterOptions);
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByName(String username) {
        return userRepository.getByName(username);
    }

    @Override
    public int showUsersCount() {
        return userRepository.getAllCount().size();
    }

    @Override
    public void create(User user) {
        boolean usernameExists = true;
        boolean emailExists = true;
        try {
            userRepository.getByName(user.getUsername());
        } catch (EntityNotFoundException e) {
            usernameExists = false;
        }
        try {
            userRepository.getEmail(user.getEmail());
        } catch (EntityNotFoundException c) {
            emailExists = false;
        }

        if (usernameExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }
        if (emailExists) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }
        user.setAdmin(false);
        user.setBanned(false);

        userRepository.create(user);
    }

    @Override
    public void update(User user) {
        userRepository.update(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }


    private void validateAdminRights(User userToCheck) {
        if (!userToCheck.isAdmin() || userToCheck.getId() != 1) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

}
