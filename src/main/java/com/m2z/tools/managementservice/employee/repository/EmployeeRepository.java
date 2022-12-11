package com.m2z.tools.managementservice.employee.repository;

import com.m2z.tools.managementservice.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository <Employee, String> {

    Optional<Employee> findEmployeeByEmail(String email);
}
