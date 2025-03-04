package com.example.ecommerce.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.ecommerce.dto.AuthRequestDTO;
import com.example.ecommerce.dto.LoginRequestDTO;
import com.example.ecommerce.dto.UserResponseDTO;
import com.example.ecommerce.model.Role;
import com.example.ecommerce.model.User;
import com.example.ecommerce.security.JwtUtil;
import com.example.ecommerce.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid AuthRequestDTO request) {
        Role role = request.getRole() != null ? request.getRole() : Role.USER;

        User user = userService.registerUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                role);

        UserResponseDTO response = new UserResponseDTO(
                user.getUsername(),
                user.getEmail(),
                user.getRole().name());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO request) {
        User user = userService.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // password check
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Password");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

}
