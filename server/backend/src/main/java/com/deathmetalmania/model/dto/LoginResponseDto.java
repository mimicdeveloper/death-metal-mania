package com.deathmetalmania.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.deathmetalmania.model.User;

/**
 * LoginResponseDto is a class used to hold both the authentication token and the user
 * information that's returned from the server to the client from a login endpoint.
 *
 * The acronym DTO is being used for "data transfer object". It means that this type of
 * class is specifically created to transfer data between the client and the server.
 */
public class LoginResponseDto {

    @NotBlank(message = "Token cannot be blank")
    @Size(min = 1, message = "Token must not be empty")
    private String token;

    @NotNull(message = "User cannot be null")
    private User user;

    public LoginResponseDto(String token, User user) {
        this.token = token;
        this.user = user;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonProperty("user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
