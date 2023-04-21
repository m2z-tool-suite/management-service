package com.m2z.tools.managementservice.security.controller;

import com.m2z.tools.managementservice.employee.model.Employee;
import com.m2z.tools.managementservice.generic.GenericResponseDTO;
import static com.m2z.tools.managementservice.generic.validation.ConstraintOrder.ValidationSequence;

import com.m2z.tools.managementservice.security.dto.AuthSignInDTO;
import com.m2z.tools.managementservice.security.dto.UserAuthoritiesResponse;
import com.m2z.tools.managementservice.security.service.AuthService;
import com.m2z.tools.managementservice.security.dto.AuthRegistrationDTO;
import com.m2z.tools.managementservice.security.validation.EmailConstraint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@Validated(ValidationSequence.class) @RequestBody AuthSignInDTO signInDto) {

        authService.signInUser(signInDto);

        return ResponseEntity.ok(GenericResponseDTO.ok());
    }

    @PutMapping("{email}/enabled")
    public GenericResponseDTO toggleUserState(@EmailConstraint @PathVariable String email, @RequestParam Boolean enabled) {
        authService.toggleUser(email, enabled);
        return GenericResponseDTO.ok();
    }

    @GetMapping("{email}/authorities")
    public UserAuthoritiesResponse getUserPAuthorities(@EmailConstraint @PathVariable String email, @RequestParam Employee.IdentityProvider idp) {

        return null;
    }
}
