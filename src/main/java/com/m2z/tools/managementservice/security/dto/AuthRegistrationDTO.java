package com.m2z.tools.managementservice.security.dto;

import com.m2z.tools.managementservice.employee.dto.NewEmployeeDTO;
import com.m2z.tools.managementservice.generic.validation.ConstraintOrder;
import com.m2z.tools.managementservice.security.validation.EmailConstraint;
import com.m2z.tools.managementservice.security.validation.PasswordConstraint;
import com.m2z.tools.managementservice.employee.validation.UniqueEmail;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRegistrationDTO {

    @UniqueEmail
    @EmailConstraint(groups = ConstraintOrder.First.class)
    private String email;

    @PasswordConstraint
    private String password;

    @NotNull
    private NewEmployeeDTO employee;
}
