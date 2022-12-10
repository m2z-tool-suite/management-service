package com.m2z.tools.managementservice.employee.model;

import com.m2z.tools.managementservice.generic.AbstractEntityUUID;

public class Employee extends AbstractEntityUUID {

    public static enum IdentityProvider {
        COGNITO
    }

    private String id;

    private IdentityProvider identityProvider;

    private String firstName;

    private String lastName;
}
