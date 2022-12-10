package com.m2z.tools.managementservice.employee.service;

import com.m2z.tools.managementservice.employee.dto.NewEmployeeDTO;
import com.m2z.tools.managementservice.employee.model.Employee;

import javax.naming.OperationNotSupportedException;

public class RelationalDBEmployeeService implements EmployeeService {
    @Override
    public Employee createEmployee(NewEmployeeDTO newEmployee) {
        throw new UnsupportedOperationException();
    }
}
