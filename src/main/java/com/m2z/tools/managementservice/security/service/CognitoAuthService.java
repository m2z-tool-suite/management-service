package com.m2z.tools.managementservice.security.service;

import com.m2z.tools.managementservice.employee.model.Employee;
import com.m2z.tools.managementservice.employee.service.EmployeeService;
import com.m2z.tools.managementservice.security.dto.AuthRegistrationDTO;
import com.m2z.tools.managementservice.security.dto.AuthSignInDTO;
import com.m2z.tools.managementservice.security.model.AuthUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CognitoAuthService implements AuthService {

    private final EmployeeService employeeService;
    private final CognitoIdentityProviderClient cognitoClient;
    @Value("${ms.aws.cognito.userPoolId}")
    private String userPoolId;

    @Override
    @Transactional
    public AuthUser registerUser(AuthRegistrationDTO newUser) {

        // Create on cognito
        UUID userId = createNewUser(newUser.getEmail(), newUser.getPassword());

        // Persist into db
        this.employeeService.createEmployee(newUser.getEmployee(), Employee.IdentityProvider.COGNITO, userId, newUser.getEmail());

        return null;
    }

    @Override
    public AuthUser signInUser(AuthSignInDTO user) {
        throw new UnsupportedOperationException();
    }

    /*
    username === email important to be defined by cloudformation else we would need username and email attribute AttributeType
     */
    private UUID createNewUser(
            String email,
            String password) {

        try {
            AdminCreateUserRequest userRequest = AdminCreateUserRequest.builder()
                    .userPoolId(userPoolId)
                    .temporaryPassword(password)
                    .username(email)
                    .messageAction("SUPPRESS")
                    .build();

            AdminCreateUserResponse response = cognitoClient.adminCreateUser(userRequest);
            log.info("User {} is created. Status: {}", email, response.user().userStatus());
            AttributeType sub = response.user().attributes().stream()
                    .filter(a ->
                            a.name().equals("sub")).findFirst()
                    .orElseThrow(() -> new RuntimeException("Missing sub from AWS response"));
            return UUID.fromString(sub.value());
        } catch (CognitoIdentityProviderException e) {
            log.error("User creation failed for {} with msg {} ", email, e.awsErrorDetails().errorMessage());
            throw new RuntimeException("AWS User creation failed");
        }
    }
}
