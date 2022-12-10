package com.m2z.tools.managementservice.security.service;

import com.m2z.tools.managementservice.employee.service.EmployeeService;
import com.m2z.tools.managementservice.security.dto.AuthRegistrationDTO;
import com.m2z.tools.managementservice.security.dto.AuthSignInDTO;
import com.m2z.tools.managementservice.security.model.AuthUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CognitoAuthService implements AuthService {

    private final EmployeeService employeeService;
    @Override
    @Transactional
    public AuthUser registerUser(AuthRegistrationDTO newUser) {

        // Create on cognito

        // Persist into db
        this.employeeService.createEmployee(newUser.getEmployee());

        return null;
    }

    @Override
    public AuthUser signInUser(AuthSignInDTO user) {
        throw new UnsupportedOperationException();
    }
}
