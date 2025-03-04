package com.example.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private String username;
    private String email;
    private String role;

    public UserResponseDTO(String username, String email, String role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
