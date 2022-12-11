package com.m2z.tools.managementservice.employee.dto;

// TODO download pictures via client assigning cognito user to s3 access user group
public record EmployeePaginationResponseDTO (String id, String emailAddress, String profilePictureUrl) {
}
