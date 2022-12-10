package com.m2z.tools.managementservice.employee.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewEmployeeDTO {

    @NotBlank
    @Size(min = 1, max = 128)
    private String firstName;
    @NotBlank
    @Size(min = 1, max = 128)
    private String lastName;
}