package com.example.wp.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/* import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; */

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* @Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private Boolean active;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<String> favouriteCategories;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<String> favouriteDestinations;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<String> visitedDestinations;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<String> wishList;

    public static User createNewUser(String username, String password) {
        User user = new User();
        user.active = true;
        user.username = username;
        user.password = password;
        user.favouriteCategories = new ArrayList<String>();
        user.favouriteDestinations = new ArrayList<String>();
        user.visitedDestinations = new ArrayList<String>();
        user.wishList = new ArrayList<String>();
        return user;
    }

    public boolean isAccountNonExpired() {
        return active;
    }

    public boolean isAccountNonLocked() {
        return active;
    }

    public boolean isCredentialsNonExpired() {
        return active;
    }

    public boolean isEnabled() {
        return active;
    }
} */
