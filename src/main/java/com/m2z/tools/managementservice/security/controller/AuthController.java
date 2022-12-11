package com.m2z.tools.managementservice.security.controller;

import com.m2z.tools.managementservice.generic.GenericResponseDTO;
import static com.m2z.tools.managementservice.generic.validation.ConstraintOrder.ValidationSequence;
import com.m2z.tools.managementservice.security.service.AuthService;
import com.m2z.tools.managementservice.security.dto.AuthRegistrationDTO;
import com.m2z.tools.managementservice.security.validation.EmailConstraint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("{email}/enabled")
    public GenericResponseDTO toggleUserState(@EmailConstraint @PathVariable String email, @RequestParam Boolean enabled) {
        authService.toggleUser(email, enabled);
        return GenericResponseDTO.ok();
    }

    public void signInUser() {

    }
}
