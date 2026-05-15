package com.hireready.securities;

import com.hireready.entities.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthoritySecurity implements GrantedAuthority {
    private Authority authority;

    @Override
    public String getAuthority() {
        if (authority == null || authority.getRole() == null) return null;
        return "ROLE_" + authority.getRole().name();
    }
}
