package com.m2z.tools.managementservice.employee.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, PARAMETER, })
@Retention(RUNTIME)
@Repeatable(ProfilePictureConstraint.List.class)
@Documented
@Constraint(validatedBy = ProfilePictureConstraintValidator.class)
public @interface ProfilePictureConstraint {

    String message() default "Bad image format";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    @Target({ FIELD, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {

        ProfilePictureConstraint[] value();
    }
}
