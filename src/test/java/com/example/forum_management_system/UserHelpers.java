package com.example.forum_management_system;

import com.example.forum_management_system.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserHelpers {


    public static User createMockAdmin(){
        User mockAdmin = createMockUser();
        mockAdmin.setAdmin(true);
        mockAdmin.setPhoneNumber("0888888888");

        return mockAdmin;
    }

    public static User createSuperAdmin(){
        User superAdmin = new User();
        superAdmin.setId(1); // Id: 1 - makes you super admin
        superAdmin.setUsername("SuperAdmin");
        superAdmin.setPassword("123456");
        superAdmin.setFirstName("SuperAdminFirstName");
        superAdmin.setLastName("SuperAdminLastName");
        superAdmin.setEmail("SuperAdminEmail");
        superAdmin.setAdmin(true);

        return superAdmin;
    }

    public static User createBannedUser(){
        User bannedUser = createMockUser();
        bannedUser.setBanned(true);

        return bannedUser;
    }

    public static User createMockUser(){
        User mockUser = new User();
        mockUser.setId(2);
        mockUser.setUsername("MockUsername");
        mockUser.setPassword("MockPassword");
        mockUser.setFirstName("MockFirstName");
        mockUser.setLastName("MockLastName");
        mockUser.setEmail("Mock@mail");

        return mockUser;
    }

    public static UserCreateDto createUserCreateDto(){
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setUsername("UserCreateDto");
        userCreateDto.setEmail("UserCreateDto@mail");
        userCreateDto.setFirstName("UserCreateFirstNameDto");
        userCreateDto.setLastName("UserCreateLastNameDto");
        userCreateDto.setPassword("UserCreateDtoPassword");

        return userCreateDto;
    }

    public static UserUpdateDto createUserUpdateDto(){
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setFirstName("UserUpdateFirstName");
        userUpdateDto.setLastName("UserUpdateLastName");
        userUpdateDto.setEmail("UserUpdate@mail");
        userUpdateDto.setPassword("UserUpdatePassword");

        return userUpdateDto;
    }

    public static AdminRightsDto adminRightsDto(){
        AdminRightsDto adminRightsDto = new AdminRightsDto();
        adminRightsDto.setAdmin(true);

        return adminRightsDto;
    }

    public static UserFilterOptions createUserFilterOptions(){
        return new UserFilterOptions("username",
                "email",
                "firstName"
        );
    }

    public static String toJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
