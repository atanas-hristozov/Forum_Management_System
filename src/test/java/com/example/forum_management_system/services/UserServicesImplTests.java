package com.example.forum_management_system.services;

import com.example.forum_management_system.UserHelpers;
import com.example.forum_management_system.exceptions.EntityNotFoundException;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.models.UserFilterOptions;
import com.example.forum_management_system.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServicesImplTests {

    @Mock
    UserRepository mockRepository;
    @InjectMocks
    UserServiceImpl mockService;


    @Test
    public void testGetAll() {
        User user = UserHelpers.createMockUser();
        User admin = UserHelpers.createMockAdmin();
        User superAdmin = UserHelpers.createSuperAdmin();

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(admin);
        users.add(superAdmin);

        UserFilterOptions userFilterOptions = UserHelpers.createUserFilterOptions();
        when(mockRepository.getAll(userFilterOptions)).thenReturn(users);

        List<User> result = mockService.getAll(userFilterOptions);
        assertEquals(users, result);
    }

    @Test
    public void testGetById() {
        User user = UserHelpers.createMockUser();

        int userId = 2;
        when(mockRepository.getById(userId)).thenReturn(user);

        User result = mockService.getById(userId);

        assertEquals(user, result);
    }

    @Test
    public void testGetByName() {
        User user = UserHelpers.createMockUser();
        String username = user.getUsername();

        when(mockService.getByName(username)).thenReturn(user);
        User result = mockService.getByName(username);

        assertEquals(user, result);
    }

    @Test
    public void testShowUsersCount(){
        User user = UserHelpers.createMockUser();
        User user2 = UserHelpers.createBannedUser();
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        when(mockRepository.getAllCount()).thenReturn(users);
        int result = mockService.showUsersCount();

        assertEquals(users.size(), result);
    }

    @Test
    public void testCreate(){
        User userToCreate = UserHelpers.createMockUser();
        when(mockRepository.getByName(userToCreate.getUsername()))
                .thenThrow(new EntityNotFoundException("User", "username", userToCreate.getUsername()));
        when(mockRepository.getEmail(userToCreate.getEmail()))
                .thenThrow(new EntityNotFoundException("User", "email", userToCreate.getEmail()));

        mockService.create(userToCreate);

        verify(mockRepository).create(userToCreate);
    }

}


