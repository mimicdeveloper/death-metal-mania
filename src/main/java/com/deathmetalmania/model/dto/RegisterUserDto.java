package com.deathmetalmania.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * RegisterUserDto is a class used to hold the registration information for a new user
 * that's sent from the client to the server for the register endpoint.
 *
 * The acronym DTO is being used for "data transfer object". It means that this type of
 * class is specifically created to transfer data between the client and the server.
 */
public class RegisterUserDto {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Confirm password cannot be empty")
    private String confirmPassword;

    @NotBlank(message = "Please select a role for this user.")
    private String role;

    public RegisterUserDto(String username, String password, String confirmPassword, String role) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getRole() {
        return role;
    }

    public boolean isPasswordsMatch() {
        return password.equals(confirmPassword);
    }
}
