package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanySummaryResponseDTO {
    private Long companyId;
    private Long userId;
    private String email;
    private String name;
    private String description;
    private Boolean enabled;
    private Integer questionBankCount;
}
