package com.insurease.controller;

import com.insurease.jwt.util.TokenProviderService; // <--- Correct Import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${app.login.username}")
    private String propUser;

    @Value("${app.login.password}")
    private String propPass;

    @Autowired
    private TokenProviderService tokenService; // <--- Injecting your Service

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        String password = request.get("password");

        // 1. Validate Credentials
        if (username != null && username.equals(propUser) && password != null && password.equals(propPass)) {
            
            // 2. Generate Token (Calling the method you just made public)
            String token = tokenService.generateToken(username);

            // 3. Return Token
            Map<String, String> response = new HashMap<>();
            response.put("message", "success");
            response.put("token", token); 

            return ResponseEntity.ok(response);

        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }
}