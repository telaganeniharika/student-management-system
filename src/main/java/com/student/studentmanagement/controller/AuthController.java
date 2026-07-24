package com.student.studentmanagement.controller;

import com.student.studentmanagement.model.User;
import com.student.studentmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:8088", allowCredentials = "true") // updated for session
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user, HttpSession session) {
        User existing = userRepo.findByUsername(user.getUsername()).orElse(null);

        if (existing != null && passwordEncoder.matches(user.getPassword(), existing.getPassword())) {
            session.setAttribute("loggedInUser", existing.getUsername()); // ✅ Set session attribute
            return ResponseEntity.ok("Login successful!");
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(409).body("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        return ResponseEntity.ok("Registered successfully!");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // 🔐 Invalidate session
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/session")
    public ResponseEntity<String> checkSession(HttpSession session) {
        Object loggedInUser = session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            return ResponseEntity.ok("User is logged in: " + loggedInUser.toString());
        } else {
            return ResponseEntity.status(401).body("User not logged in");
        }
    }
}
