package com.m2z.tools.managementservice.security.dto;

import com.m2z.tools.managementservice.employee.dto.NewEmployeeDTO;
import com.m2z.tools.managementservice.security.validation.EmailConstraint;
import com.m2z.tools.managementservice.security.validation.PasswordConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRegistrationDTO {

    @EmailConstraint
    private String email;

    @PasswordConstraint
    private String password;

    private NewEmployeeDTO employee;
}
