package com.m2z.tools.managementservice.employee.service;

import com.m2z.tools.managementservice.employee.dto.NewEmployeeDTO;
import com.m2z.tools.managementservice.employee.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RelationalDBEmployeeService implements EmployeeService {
    @Override
    public Employee createEmployee(NewEmployeeDTO newEmployee) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<Employee> page(Pageable pageable) {
        throw new UnsupportedOperationException();
    }
}
