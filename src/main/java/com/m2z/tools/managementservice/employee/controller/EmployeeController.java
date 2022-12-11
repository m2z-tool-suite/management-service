package com.m2z.tools.managementservice.employee.controller;

import com.m2z.tools.managementservice.employee.dto.EmployeePaginationResponseDTO;
import com.m2z.tools.managementservice.employee.model.Employee;
import com.m2z.tools.managementservice.employee.service.EmployeeService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@ResponseBody
@RequestMapping("api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public Page<EmployeePaginationResponseDTO> listUsers(
            @RequestParam(required = false, defaultValue = "0") int page,
            @Min(value = 10) @Max(value = 10) @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String id,
            @RequestParam(defaultValue = "ASC") Sort.Direction order
    ) {


        PageRequest pageRequest = PageRequest.of(page, size, order, "createdAt");
        Page<Employee> employeePage = employeeService.page(
                pageRequest,
                email,
                id
        );


        return new PageImpl<>(
                employeePage.getContent().stream().map( e ->
                        new EmployeePaginationResponseDTO(
                                e.getId(),
                                e.getEmail(),
                                "presigned-url"))
                        .collect(Collectors.toList()),
                pageRequest,
                employeePage.getTotalElements()
        );
    }
}
