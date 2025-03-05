package com.example.ecommerce.service;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.security.JwtUtil;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager; 
    
    public AuthService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public String register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Simulasi save ke DB dengan ID baru
        User savedUser = userRepository.save(user);
        if (savedUser.getId() == null) {
            throw new RuntimeException("Failed to save user");
        }

        return jwtUtil.generateToken(savedUser);
    }

    public String login(String username, String password) {
        // Cek apakah user ada
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        // Cek password match atau tidak
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Cek apakah authenticationManager ada sebelum dipakai
        if (authenticationManager != null) {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
        }

        return jwtUtil.generateToken(user);
    }
}
