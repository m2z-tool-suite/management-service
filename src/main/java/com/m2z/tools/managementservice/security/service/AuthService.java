package com.m2z.tools.managementservice.security.service;

import com.m2z.tools.managementservice.security.dto.AuthRegistrationDTO;
import com.m2z.tools.managementservice.security.dto.AuthSignInDTO;
import com.m2z.tools.managementservice.security.model.AuthUser;

public interface AuthService {

    AuthUser registerUser(AuthRegistrationDTO newUser);

    // Not done on client side for this specific service
    AuthUser signInUser(AuthSignInDTO user);
}
