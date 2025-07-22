package com.deathmetalmania.dao;

import com.deathmetalmania.model.User;
import com.deathmetalmania.model.UserProfile;
import com.deathmetalmania.model.dto.RegisterUserDto;
import com.deathmetalmania.model.dto.UserProfileDto;

import java.util.List;

public interface UserDao {
    UserProfile getUserProfile(String username);
    List<User> getUsers();
    User getUserById(int userId);
    User getUserByUsername(String username);
    User createUser(User newUser);
    User createUserWithProfile(RegisterUserDto dto);
    UserProfile getUserProfileByUsername(String username);
    UserProfile updateUserProfile(int userId, UserProfileDto userProfile);
    List<UserProfile> getAllUserProfiles();
    UserProfile getUserProfileById(int id);
    void deleteUserById(int id);
}
