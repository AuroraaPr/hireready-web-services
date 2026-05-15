package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterCompanyRequestDTO {
    private String email;
    private String password;
    private String name;
    private String description;
}
