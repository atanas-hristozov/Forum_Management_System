package com.example.forum_management_system.repositories;

import com.example.forum_management_system.Models.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    User getById(int id);

    User getByName(String username);

    void create(User user);

    void update (User user);


}
