package com.example.forum_management_system.helpers;

import com.example.forum_management_system.models.User;
import com.example.forum_management_system.models.userDtos.AdminUpdateDto;
import com.example.forum_management_system.models.userDtos.UserCreateDto;
import com.example.forum_management_system.models.userDtos.AdminRightsDto;
import com.example.forum_management_system.models.userDtos.UserUpdateDto;
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

    public User fromAdminRightsDto(int id, AdminRightsDto dto){
        User user = userService.getById(id);
        user.setAdmin(dto.isAdmin());
        user.setBanned(dto.isBanned());

        return user;
    }

    public User fromAdminUpdateDto(int id, AdminUpdateDto adminUpdateDto){
        User user = userService.getById(id);
        user.setFirstName(adminUpdateDto.getFirstName());
        user.setLastName(adminUpdateDto.getLastName());
        user.setEmail(adminUpdateDto.getEmail());
        user.setPassword(adminUpdateDto.getPassword());
        user.setPhoneNumber(adminUpdateDto.getPhoneNumber());

        return user;
    }

}
