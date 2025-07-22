package com.deathmetalmania.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

/**
 * UserProfileDto is a class used to hold the user profile information.
 */
public class UserProfileDto {

    @JsonProperty("firstName")
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @JsonProperty("lastName")
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    public UserProfileDto(String first_name, String last_name, String email) {
        this.firstName = first_name;
        this.lastName = last_name;
        this.email = email;
    }

    public String getFirstname() {
        return firstName;
    }

    public String getLastname() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
