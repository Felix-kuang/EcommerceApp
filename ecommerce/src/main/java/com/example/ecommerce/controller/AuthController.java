package com.example.ecommerce.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.ecommerce.dto.AuthRequestDTO;
import com.example.ecommerce.dto.LoginRequestDTO;
import com.example.ecommerce.dto.UserResponseDTO;
import com.example.ecommerce.exception.UserNotFoundException;
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
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO request) {
        User user = userService.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException());

        // password check
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Password");
        }

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

}
