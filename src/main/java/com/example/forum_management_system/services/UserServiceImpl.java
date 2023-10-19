package com.example.forum_management_system.services;

import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

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
        boolean usernameExists = true;
        try {
            userRepository.getByName(user.getUsername());
        } catch (EntityNotFoundException e) {
            usernameExists = false;
        }

        if (usernameExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
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

    @Override
    public void updateToAdmin(User user) {
       userRepository.update(user);
    }
}
