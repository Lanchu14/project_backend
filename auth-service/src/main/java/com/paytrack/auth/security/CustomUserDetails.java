package com.paytrack.auth.security;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.paytrack.auth.entity.User;

public class CustomUserDetails
        implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // SonarQube Fix
    private final transient User user;

    public CustomUserDetails(User user) {

        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority>
    getAuthorities() {

        // DYNAMIC ROLE
        return List.of(
                () -> user.getRole().name()
        );
    }

    @Override
    public String getPassword() {

        return user.getPassword();
    }

    @Override
    public String getUsername() {

        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }
}