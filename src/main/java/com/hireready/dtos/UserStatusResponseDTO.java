package com.hireready.dtos;

import com.hireready.enums.AuthorityRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusResponseDTO {
    private Long userId;
    private String email;
    private AuthorityRole role;
    private Boolean enabled;
}
