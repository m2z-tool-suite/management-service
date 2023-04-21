package com.m2z.tools.managementservice.security.dto;

import com.m2z.tools.managementservice.security.validation.EmailConstraint;
import com.m2z.tools.managementservice.security.validation.PasswordConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthSignInDTO {

    @EmailConstraint
    private String email;
    @PasswordConstraint
    private String password;
}
