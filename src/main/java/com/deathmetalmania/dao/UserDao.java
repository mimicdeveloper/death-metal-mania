package com.deathmetalmania.dao;

import com.deathmetalmania.model.User;
import com.deathmetalmania.model.UserProfile;
import com.deathmetalmania.model.dto.UserProfileDto;

import java.util.List;

public interface UserDao {
    /**
     * Get user profile detail by username
     * @param username
     * @return profile details
     */
    UserProfile getUserProfile(String username);

    /**
     * Get all users available
     * @return list of users, empty list if none
     */
    List<User> getUsers();

    /**
     * Get user detail by ID
     * @param userId
     * @return user by ID
     */
    User getUserById(int userId);

    /**
     * Get user detail by username
     * @param username
     * @return user profile
     */
    User getUserByUsername(String username);

    /**
     * Create a single new user
     * @param newUser
     * @return newly created user
     */
    User createUser(User newUser);

    /**
     * Get user profile by username
     * @param username
     * @return user profile
     */
    UserProfile getUserProfileByUsername(String username);

    /**
     * Update user profile by ID
     * @param userId
     * @param userProfile
     * @return
     */
    UserProfile updateUserProfile(int userId, UserProfileDto userProfile);

    /**
     * Get all users available
     * @return list of user profiles, empty list if none
     */
    List<UserProfile> getAllUserProfiles();

    /**
     * Get user profile by ID
     * @param id
     * @return user profile
     */
    UserProfile getUserProfileById(int id);

    /**
     * Delete User by ID
     * @param id
     */
    void deleteUserById(int id);
}
