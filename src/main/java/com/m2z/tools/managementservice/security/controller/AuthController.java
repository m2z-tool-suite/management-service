package com.m2z.tools.managementservice.security.controller;

import com.m2z.tools.managementservice.generic.GenericResponseDTO;
import static com.m2z.tools.managementservice.generic.validation.ConstraintOrder.ValidationSequence;
import com.m2z.tools.managementservice.security.service.AuthService;
import com.m2z.tools.managementservice.security.dto.AuthRegistrationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
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
    public GenericResponseDTO registerUser(@Validated(ValidationSequence.class) @RequestBody AuthRegistrationDTO registrationDTO) {

        authService.registerUser(registrationDTO);

        return GenericResponseDTO.created("User registered successfully");
    }

    public void signInUser() {

    }
}
