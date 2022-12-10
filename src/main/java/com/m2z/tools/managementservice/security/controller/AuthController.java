package com.m2z.tools.managementservice.security.controller;

import com.m2z.tools.managementservice.generic.GenericResponse;
import com.m2z.tools.managementservice.security.service.AuthService;
import com.m2z.tools.managementservice.security.dto.AuthRegistrationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("api/v1/auth")
@ResponseBody
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    public GenericResponse registerUser(@RequestBody AuthRegistrationDTO registrationDTO) {

        authService.registerUser(registrationDTO);

        return GenericResponse.created("User registered successfully");
    }

    public void signInUser() {

    }
}
