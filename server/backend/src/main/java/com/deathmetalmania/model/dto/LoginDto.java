package com.deathmetalmania.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * LoginDto is a class used to hold the user login information that's sent from the
 * client to the server for the login endpoint.
 *
 * The acronym DTO is being used for "data transfer object". It means that this type of
 * class is specifically created to transfer data between the client and the server.
 */
public class LoginDto {

   @NotBlank(message = "Username cannot be blank")
   @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
   private String username;

   @NotBlank(message = "Password cannot be blank")
   @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
   private String password;

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   @Override
   public String toString() {
      return "LoginDTO{" +
              "username='" + username + '\'' +
              ", password='" + password + '\'' +
              '}';
   }
}
