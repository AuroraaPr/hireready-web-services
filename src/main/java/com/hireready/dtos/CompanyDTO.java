package com.hireready.dtos;

import lombok.Data;

@Data
public class CompanyDTO {
    private String name;
    private String description;
    private String email;
    private String password;
}