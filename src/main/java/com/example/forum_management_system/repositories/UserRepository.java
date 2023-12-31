package com.example.forum_management_system.repositories;


import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.models.UserFilterOptions;

import java.util.List;
import java.util.Set;

public interface UserRepository {

    List<User> getAll(UserFilterOptions userFilterOptions);
    List<User> getAllCount();
    User getById(int id);
    User getByName(String username);
    User getEmail(String email);
    void create(User user);
    void update (User user);
    void delete (User user);
}
