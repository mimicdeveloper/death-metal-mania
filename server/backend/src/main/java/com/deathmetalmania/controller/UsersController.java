package com.deathmetalmania.controller;

import com.deathmetalmania.dao.UserDao;
import com.deathmetalmania.exception.InvalidDataException;
import com.deathmetalmania.model.UserProfile;
import com.deathmetalmania.model.dto.UserProfileDto;
import com.deathmetalmania.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UsersController {

    private final UserService userService;
    private final UserDao userDao;

    public UsersController(UserService userService, UserDao userDao) {
        this.userService = userService;
        this.userDao = userDao;
    }

    // Get User Profile
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public UserProfile getUserProfile(Principal principal) {
        String username = principal.getName();
        UserProfile userProfile = userDao.getUserProfile(username);

        if (userProfile == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User profile not found");
        }
        return userProfile;
    }

    // Update the authenticated user's profile
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.PUT)
    public UserProfile updateUserProfile(Principal principal, @RequestBody UserProfileDto profile) {
        try {
            return userService.updateProfile(principal.getName(), profile);
        } catch (InvalidDataException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user profile data", e);
        }
    }
}
