package com.m2z.tools.managementservice.employee.validation;

import com.m2z.tools.managementservice.employee.repository.EmployeeRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;


public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && employeeRepository.findEmployeeByEmail(value).isEmpty();
    }
}
