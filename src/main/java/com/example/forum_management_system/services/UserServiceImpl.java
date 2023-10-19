package com.example.forum_management_system.services;

import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.exceptions.EntityDuplicateException;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.models.UserFilterOptions;
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
        user.setLiked(false);
        user.setDisliked(false);
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

   /* @Override
    public void promoteAdmin(User user, int id) {
        validateAdmin(user);
            User userToUpdate = userRepository.getById(id);
            if (userToUpdate.isAdmin()){
                throw new EntityDuplicateException("Admin", "id", String.valueOf(id));
            }
            userToUpdate.setAdmin(true);

            userRepository.update(userToUpdate);
    }

    @Override
    public void demoteAdmin(User user, int id) {
        validateAdmin(user);
        User userToUpdate = userRepository.getById(id);
        if (userToUpdate.isAdmin()){
            throw new EntityDuplicateException("Admin", "id", String.valueOf(id));
        }
        userToUpdate.setAdmin(false);

        userRepository.update(userToUpdate);
    }

    @Override
    public void banUser(User user, int id) {
        validateAdmin(user);
        User userToUpdate = userRepository.getById(id);
        if (userToUpdate.isAdmin()){
            throw new EntityDuplicateException("Admin", "id", String.valueOf(id));
        }
        userToUpdate.setBanned(true);

        userRepository.update(userToUpdate);
    }

    @Override
    public void unbanUser(User user, int id) {
        validateAdmin(user);
        User userToUpdate = userRepository.getById(id);
        if (userToUpdate.isAdmin()){
            throw new EntityDuplicateException("Admin", "id", String.valueOf(id));
        }
        userToUpdate.setAdmin(false);

        userRepository.update(userToUpdate);
    }*/

    private void validateAdmin(User userToCheck){
        if (!userToCheck.isAdmin()){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

}
