package com.m2z.tools.managementservice.security.dto;

import com.m2z.tools.managementservice.employee.dto.NewEmployeeDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRegistrationDTO {
    private String email;
    private String password;

    private NewEmployeeDTO employee;
}
