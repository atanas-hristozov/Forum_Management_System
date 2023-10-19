package com.example.forum_management_system.services;

import com.example.forum_management_system.models.User;
import com.example.forum_management_system.models.UserFilterOptions;

import java.util.List;

public interface UserService {

    List<User> getAll(UserFilterOptions userFilterOptions);
    User getById(int id);
    User getByName (String username);
    int showUsersCount();
    void create(User user);
    void update(User user);
    void delete(User user);
    /*void promoteAdmin(User user, int id);
    void demoteAdmin(User user, int id);
    void banUser(User user, int id);
    void unbanUser(User user, int id);*/

}
