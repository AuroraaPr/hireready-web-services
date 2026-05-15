package com.hireready.securities;

import com.hireready.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSecurity implements UserDetails {
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getAuthority() == null) return List.of();
        return List.of(new AuthoritySecurity(user.getAuthority()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override public boolean isAccountNonExpired(){ return UserDetails.super.isAccountNonExpired(); }

    @Override public boolean isAccountNonLocked(){ return UserDetails.super.isAccountNonLocked(); }

    @Override public boolean isCredentialsNonExpired(){ return UserDetails.super.isCredentialsNonExpired(); }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getEnabled());
    }
}
