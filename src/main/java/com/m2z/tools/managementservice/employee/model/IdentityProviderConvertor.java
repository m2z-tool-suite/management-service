package com.m2z.tools.managementservice.employee.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class IdentityProviderConvertor implements AttributeConverter <Employee.IdentityProvider, Character> {
    @Override
    public Character convertToDatabaseColumn(Employee.IdentityProvider attribute) {
        if (attribute == null) return null;

        return attribute.getCode();
    }

    @Override
    public Employee.IdentityProvider convertToEntityAttribute(Character dbData) {
        if (dbData == null) return null;
        return Employee.IdentityProvider.of(dbData);
    }
}
