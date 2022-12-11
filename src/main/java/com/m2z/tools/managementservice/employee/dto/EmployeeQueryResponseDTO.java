package com.m2z.tools.managementservice.employee.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EmployeeQueryResponseDTO(String id, String email, String firstName, String lastName,
                                       LocalDateTime createdAt, Boolean enabled) {
}
