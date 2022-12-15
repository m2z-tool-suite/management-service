package com.m2z.tools.managementservice.employee.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder // Delte EmployeePaginationResponseDTO use builder to send less values
public record EmployeeQueryResponseDTO(String id, String email, String firstName, String lastName, String profilePictureUrl, LocalDateTime createdAt, Boolean enabled) {
}
