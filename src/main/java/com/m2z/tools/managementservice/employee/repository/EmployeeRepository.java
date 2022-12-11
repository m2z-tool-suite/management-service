package com.m2z.tools.managementservice.employee.repository;

import com.m2z.tools.managementservice.employee.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository <Employee, String> {

    Optional<Employee> findEmployeeByEmail(String email);

    @Query("""
    SELECT employee FROM Employee employee
    WHERE (:id IS NULL OR UPPER(employee.id) LIKE UPPER(concat('%',:id,'%') ) )
    AND (:email IS NULL OR UPPER(employee.email) LIKE UPPER(concat('%',:email,'%') ) )
    """)
    Page<Employee> findAll(Pageable pageable, String email, String id);
}
