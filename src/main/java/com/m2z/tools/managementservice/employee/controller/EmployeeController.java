package com.m2z.tools.managementservice.employee.controller;

import com.m2z.tools.managementservice.employee.dto.EmployeeQueryResponseDTO;
import com.m2z.tools.managementservice.employee.model.Employee;
import com.m2z.tools.managementservice.employee.repository.EmployeeRepository;
import com.m2z.tools.managementservice.employee.service.EmployeeService;
import com.m2z.tools.managementservice.employee.service.ProfilePictureStorage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Validated
@ResponseBody
@RequestMapping("api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;

    private final ProfilePictureStorage profilePictureStorage;

    @GetMapping
    @PreAuthorize("hasAuthority('MS_L_USERS')")
    public Page<EmployeeQueryResponseDTO> listUsers(
            @RequestParam(required = false, defaultValue = "0") int page,
            @Min(value = 1) @Max(value = 100) @RequestParam(required = false, defaultValue = "10") int size,
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
                employeePage.getContent().stream().map(e -> {
                            Optional<URL> url = profilePictureStorage.generateUrl(e.getId());
                            return EmployeeQueryResponseDTO.builder()
                                    .id(e.getId())
                                    .firstName(e.getFirstName())
                                    .lastName(e.getLastName())
                                    .createdAt(e.getCreatedAt())
                                    .enabled(e.isEnabled())
                                    .email(e.getEmail())
                                    .profilePictureUrl((url.map(URL::toString).orElse(null))).build();
                        }
                ).collect(Collectors.toList()),
                pageRequest,
                employeePage.getTotalElements()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MS_L_USERS')") // OR isSelf(#id)
    public EmployeeQueryResponseDTO getById(@PathVariable String id) {
        Employee em = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        return EmployeeQueryResponseDTO.builder()
                .id(em.getId())
                .email(em.getEmail())
                .profilePictureUrl((profilePictureStorage.generateUrl(id).map(URL::toString).orElse(null)))
                .firstName(em.getFirstName())
                .lastName(em.getLastName())
                .createdAt(em.getCreatedAt())
                .enabled(em.isEnabled())
                .build();
    }
}
