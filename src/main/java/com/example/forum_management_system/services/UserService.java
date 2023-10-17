package com.example.forum_management_system.services;

import com.example.forum_management_system.models.User;

import java.util.List;

public interface UserService {

    List<User> getAll();
    User getById(int id);
    User getByName (String username);
    int showUsersCount();



}
