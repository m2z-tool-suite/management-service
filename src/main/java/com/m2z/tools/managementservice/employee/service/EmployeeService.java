package com.m2z.tools.managementservice.employee.service;

import com.m2z.tools.managementservice.employee.dto.NewEmployeeDTO;
import com.m2z.tools.managementservice.employee.model.Employee;

public interface EmployeeService {

    Employee createEmployee(NewEmployeeDTO newEmployee);
}
