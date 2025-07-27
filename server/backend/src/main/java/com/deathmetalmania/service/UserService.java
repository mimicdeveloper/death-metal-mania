package com.deathmetalmania.service;

import com.deathmetalmania.dao.UserDao;
import com.deathmetalmania.model.User;
import com.deathmetalmania.model.UserProfile;
import com.deathmetalmania.model.dto.UserProfileDto;
import com.deathmetalmania.exception.ServiceException; // Import the custom ServiceException
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserProfile getUserProfile(String username) {
        UserProfile userProfile = userDao.getUserProfileByUsername(username);
        if (userProfile == null) {
            throw new ServiceException("User profile not found for username: " + username);
        }
        return userProfile;
    }

    public UserProfile updateProfile(String username, UserProfileDto userProfileDto) {
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new ServiceException("User not found: " + username);
        }

        int userId = user.getId();
        UserProfile updatedProfile = userDao.updateUserProfile(userId, userProfileDto);
        if (updatedProfile == null) {
            throw new ServiceException("Error updating profile for user: " + username);
        }
        return updatedProfile;
    }

    public int getUserIdByUsername(String username) {
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new ServiceException("User not found: " + username);
        }
        return user.getId();
    }
}
