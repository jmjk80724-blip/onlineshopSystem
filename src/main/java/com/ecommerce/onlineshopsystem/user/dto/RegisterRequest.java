package com.ecommerce.onlineshopsystem.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "username is required")
    @Size(min=3, max = 20, message = "Username must be 3 -20 characters")
    private String username;

    @NotBlank(message = "password is required")
    @Size(min=6, max= 20, message = "Password must be at leat 6 characters")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String phone;

    private String fullName;
}
