package com.m2z.tools.managementservice.employee.service;

import com.m2z.tools.managementservice.employee.dto.NewEmployeeDTO;
import com.m2z.tools.managementservice.employee.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EmployeeService {

    Page<Employee> page(Pageable pageable);

    Employee createEmployee(NewEmployeeDTO employee, Employee.IdentityProvider cognito, UUID userId, String email);
}
