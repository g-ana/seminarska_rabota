package com.example.wp.backend.web.rest_controllers;

/* import com.example.wp.backend.service.AuthService;
import com.example.wp.backend.service.UserDetailsService; */
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/* @RestController
@CrossOrigin("http://localhost:3000")
public class LoginController {

    private final AuthService authService;
    private final UserDetailsService userDetailsService;

    public LoginController(AuthService authService, UserDetailsService userDetailsService) {
        this.authService = authService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public String login() {

    }

    @PostMapping("/logout")
    public String logout() {

    }

    @PostMapping("/signup")
    public String signUp(@RequestParam String username, @RequestParam String password,
                         @RequestParam String repeatedPassword) {
        String message = "";
        try {
            this.authService.signUpUser(username, password, repeatedPassword);
            message = "Successful registration";
            return message;
        }
        catch (RuntimeException ex) {
            message = ex.getMessage();
            return message;
        }
    }
} */
