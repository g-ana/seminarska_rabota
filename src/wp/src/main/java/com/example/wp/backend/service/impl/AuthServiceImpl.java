package com.example.wp.backend.service.impl;

/* import com.example.wp.backend.model.User;
import com.example.wp.backend.repository.impl.jpa.JpaUserRepository;
import com.example.wp.backend.service.AuthService;
import com.example.wp.backend.service.UserDetailsService; */
/* import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder; */
import org.springframework.stereotype.Service;

/* @Service
public class AuthServiceImpl implements AuthService {

    private final JpaUserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(JpaUserRepository userRepository,
                           UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Long getCurrentUserId() {
        return this.getCurrentUser().getId();
    }

    public String getCurrentUserUsername() {
        return this.getCurrentUser().getUsername();
    }

    public User signUpUser(String username, String password, String repeatedPassword) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(this.passwordEncoder.encode(password));
        return this.userDetailsService.registerUser(user);
    }
} */
