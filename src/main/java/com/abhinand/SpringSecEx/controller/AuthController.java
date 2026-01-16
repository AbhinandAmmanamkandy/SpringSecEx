package com.abhinand.SpringSecEx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.abhinand.SpringSecEx.model.User;
import com.abhinand.SpringSecEx.repository.UserRepository;

@Controller
public class AuthController {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("auth/login")
    public String login() {
        return "login";
    }

    @GetMapping("auth/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("auth/register")
    public String registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return "redirect:/auth/login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

}
