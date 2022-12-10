package com.m2z.tools.managementservice.security.dto;

import com.m2z.tools.managementservice.employee.dto.NewEmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRegistrationDTO {
    private String email;
    private String password;

    private NewEmployeeDTO employee;
}
