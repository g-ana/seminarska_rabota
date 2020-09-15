package com.example.wp.backend.service.impl;

/* import com.example.wp.backend.model.User;
import com.example.wp.backend.model.exceptions.UserAlreadyExistsException;
import com.example.wp.backend.model.exceptions.UserNotFoundException;
import com.example.wp.backend.repository.impl.jpa.JpaUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException; */
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/* @Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final JpaUserRepository userRepository;

    public UserDetailsServiceImpl(JpaUserRepository userRepository) {
        this.userRepository=userRepository;
    }

    public User findUserById(Long id) throws UserNotFoundException {
        return this.userRepository.findUserById(id);
    }

    public User registerUser(User user) throws UserAlreadyExistsException {
        if (this.userRepository.existsById(user.getId())) {
            throw new UserAlreadyExistsException(user.getUsername());
        }
        return this.userRepository.save(user);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username);
    }
} */
