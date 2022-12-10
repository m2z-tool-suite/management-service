package com.m2z.tools.managementservice.security.controller;

import com.m2z.tools.managementservice.generic.GenericResponse;
import com.m2z.tools.managementservice.security.service.AuthService;
import com.m2z.tools.managementservice.security.dto.AuthRegistrationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice("api/v1/auth/")
@ResponseBody
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    public GenericResponse registerUser(@RequestBody AuthRegistrationDTO registrationDTO) {

        authService.registerUser(registrationDTO);

        return GenericResponse.ok();
    }

    public void signInUser() {

    }
}
