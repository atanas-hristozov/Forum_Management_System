package com.example.forum_management_system.helpers;

import com.example.forum_management_system.models.User;
import com.example.forum_management_system.models.UserCreateDto;
import com.example.forum_management_system.models.UserRightsDto;
import com.example.forum_management_system.models.UserUpdateDto;
import com.example.forum_management_system.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final UserService userService;

    @Autowired
    public UserMapper(UserService userService) {
        this.userService = userService;
    }



    public User fromUserCreateDto(UserCreateDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());

        return user;
    }

    public User fromUserUpdateDto(int id, UserUpdateDto dto) {
        User user = userService.getById(id);
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return user;
    }

    public User fromUserRightsDto(int id, UserRightsDto dto){
        User user = userService.getById(id);
        user.setAdmin(dto.isAdmin());
        user.setBanned(dto.isBanned());

        return user;
    }
}
