package com.hireready.securities;

import com.hireready.entities.Authority;
import com.hireready.enums.AuthorityRole;
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
        // Se valida que no sea nulo
        if (authority == null || authority.getRole() == null) {
            return null;
        }
        // Se pasa el rol que esta como Enum a String
        return "ROLE_" + authority.getRole().name();
    }

}
