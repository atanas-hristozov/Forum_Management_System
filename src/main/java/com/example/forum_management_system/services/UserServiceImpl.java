package com.example.forum_management_system.services;

import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    public static final String INVALID_AUTHENTICATION = "Invalid authentication!";

    private final UserRepository userRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
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
        return userRepository.getAll().size();
    }

    @Override
    public void create(User user) {
        boolean duplicateExists = true;
        try {
            userRepository.getByName(user.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }

        userRepository.create(user);
    }

    @Override
    public void update(User user) {
        boolean duplicateExists = true;
        try {
            User existingUsername = userRepository.getByName(user.getUsername());
            if (existingUsername.getId() == user.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }

        userRepository.update(user);
    }

    @Override
    public void delete(int id, User user) {
        if (user.getId() != id)
            throw new AuthorizationException(INVALID_AUTHENTICATION);

        userRepository.delete(user);
    }

    private boolean isAdmin(User user){
        if (!user.isAdmin())
            throw new AuthorizationException(INVALID_AUTHENTICATION);

        return true;
    }
}
