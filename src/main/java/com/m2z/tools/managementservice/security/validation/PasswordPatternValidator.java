package com.m2z.tools.managementservice.security.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordPatternValidator implements ConstraintValidator <PasswordConstraint, String> {

    private static final Pattern passwordPattern = Pattern.compile("^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{8,128}$");

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && passwordPattern.matcher(password).find();
    }
}
