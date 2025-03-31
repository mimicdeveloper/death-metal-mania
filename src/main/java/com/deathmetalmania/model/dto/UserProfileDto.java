package com.deathmetalmania.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

/**
 * UserProfileDto is a class used to hold the user profile information.
 */
public class UserProfileDto {

    @JsonProperty("first_name")
    @NotBlank(message = "First name cannot be blank")
    private String firstname;

    @JsonProperty("last_name")
    @NotBlank(message = "Last name cannot be blank")
    private String lastname;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    public UserProfileDto(String first_name, String last_name, String email) {
        this.firstname = first_name;
        this.lastname = last_name;
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }
}
