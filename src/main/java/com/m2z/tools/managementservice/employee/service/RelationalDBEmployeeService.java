package com.m2z.tools.managementservice.employee.service;

import com.m2z.tools.managementservice.employee.dto.NewEmployeeDTO;
import com.m2z.tools.managementservice.employee.model.Employee;
import com.m2z.tools.managementservice.employee.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class RelationalDBEmployeeService implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    @Override
    @Transactional
    public Employee save(NewEmployeeDTO employee, Employee.IdentityProvider identityProvider, String userId, String email) {
        log.info("Persisting user: {} provider: {}", email, identityProvider);

        return employeeRepository.save(new Employee(userId, email, identityProvider, employee.getFirstName(), employee.getLastName(), LocalDateTime.now()));
    }

    @Override
    public Page<Employee> page(Pageable pageable, String email, String id) {
        return employeeRepository.findAll(pageable, email, id);
    }
}
