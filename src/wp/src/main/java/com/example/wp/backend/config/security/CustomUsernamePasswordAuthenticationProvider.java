package com.example.wp.backend.config.security;

// import com.example.wp.backend.service.impl.UserDetailsServiceImpl;
/* import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder; */
import org.springframework.stereotype.Component;

/* @Component
public class CustomUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsServiceImpl userDetailsService;

    private PasswordEncoder passwordEncoder;

    public CustomUsernamePasswordAuthenticationProvider(
            UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        if (username.equals("") || password.equals("")) {
            throw new BadCredentialsException("Invalid credentials! ");
        }
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Password is incorrect! ");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());
    }

    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
} */
