package com.example.forum_management_system.services;

import com.example.forum_management_system.Models.User;
import com.example.forum_management_system.repositories.CommentRepository;
import com.example.forum_management_system.repositories.UserRepository;

import java.util.List;


public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;


    public UserServiceImpl(UserRepository userRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
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
}
