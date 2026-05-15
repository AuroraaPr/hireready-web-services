package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompanyResponseDTO {
    private Long companyId;
    private Long userId;
    private String email;
    private String name;
    private String description;
}