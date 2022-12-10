package com.m2z.tools.managementservice.generic;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class GenericResponse {

    @Getter private final String message;
    @Getter private final int statusCode;
    @Getter private final LocalDateTime time;

    private GenericResponse(String message, int statusCode, LocalDateTime time) {
        this.message = message;
        this.statusCode = statusCode;
        this.time = time;
    }

    public static GenericResponse ok() {
        return ok("Operation completed successfully");
    }

    public static GenericResponse ok(final String message) {
        return createWithDefaultTime(message, HttpStatus.OK.value());
    }

    public static GenericResponse created() {
        return created("Object created successfully");
    }

    public static GenericResponse created(final String message) {
        return createWithDefaultTime(message, HttpStatus.CREATED.value());
    }

    public static GenericResponse bad() {
        return createWithDefaultTime("Something went wrong", HttpStatus.BAD_REQUEST.value());
    }

    public static GenericResponse notFound() {
        return createWithDefaultTime("Requested resource not found", HttpStatus.NOT_FOUND.value());
    }

    private static GenericResponse createWithDefaultTime(final String message, final int statusCode) {
        return new GenericResponse(message, statusCode, LocalDateTime.now());
    }
}
