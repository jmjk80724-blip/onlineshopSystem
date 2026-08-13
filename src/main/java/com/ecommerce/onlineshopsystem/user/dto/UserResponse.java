package com.ecommerce.onlineshopsystem.user.dto;

import lombok.Data;


import java.time.LocalDateTime;

@Data
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String phone;
    private String fullName;
    private String role;
    private LocalDateTime createdAt;
}
