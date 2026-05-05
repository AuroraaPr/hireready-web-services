package com.hireready.dtos;

import com.hireready.enums.AuthorityRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponseDTO {
    private Long userId;
    private String email;
    private AuthorityRole role;
    private Long applicantId;
    private Long companyId;
}
