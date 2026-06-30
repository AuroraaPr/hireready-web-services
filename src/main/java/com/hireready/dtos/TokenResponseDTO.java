package com.hireready.dtos;

import com.hireready.enums.AuthorityRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDTO {
    private String jwt;
    private Long userId;
    private String email;
    private String name;
    private AuthorityRole role;
    private Long applicantId;
    private Long companyId;
}
