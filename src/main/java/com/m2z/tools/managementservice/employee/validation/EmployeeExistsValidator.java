package com.m2z.tools.managementservice.employee.validation;

import com.m2z.tools.managementservice.employee.repository.EmployeeRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class EmployeeExistsValidator implements ConstraintValidator<EmployeeExists, String> {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && employeeRepository.findById(value).isPresent();
    }
}
