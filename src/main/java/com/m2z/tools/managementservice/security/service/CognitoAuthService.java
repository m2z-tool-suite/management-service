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
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

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
    public Employee registerUser(AuthRegistrationDTO newUser) {

        // Create on cognito
        String userId = createNewUser(newUser.getEmail(), newUser.getPassword());

        // Persist into db
        return this.employeeService.save(newUser.getEmployee(), Employee.IdentityProvider.COGNITO, userId, newUser.getEmail());
    }

    @Override
    public AuthUser signInUser(AuthSignInDTO user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void toggleUser(String email, Boolean enabled) {
        try {
            if (enabled) {
                log.info("Cognito:Enabling user email: {}", email);
                AdminEnableUserRequest request = AdminEnableUserRequest.builder()
                        .userPoolId(userPoolId)
                        .username(email)
                        .build();
                cognitoClient.adminEnableUser(request);
            } else {
                log.info("Cognito:Disabling user email: {}", email);
                AdminDisableUserRequest request = AdminDisableUserRequest.builder()
                        .userPoolId(userPoolId)
                        .username(email)
                        .build();
                cognitoClient.adminDisableUser(request);
            }
        } catch (ResourceNotFoundException e) {
            log.error("Cognito:Resource not found msg: {}", e.getMessage());
            throw new RuntimeException();
        } catch (UserNotFoundException e) {
            log.error("Cognito:User not fond email: {} msg: {}", email, e.getMessage());
            throw new RuntimeException();
        }
        employeeService.toggleUserByEmail(email, enabled);
    }

    /*
    username === email important to be defined by cloudformation else we would need username and email attribute AttributeType
     */
    private String createNewUser(
            String email,
            String password) {

        try {
            log.info("Cognito:Creating user {}", email);
            AdminCreateUserRequest userRequest = AdminCreateUserRequest.builder()
                    .userPoolId(userPoolId)
                    .temporaryPassword(password)
                    .username(email)
                    .messageAction("SUPPRESS")
                    .build();

            AdminCreateUserResponse response = cognitoClient.adminCreateUser(userRequest);
            log.info("Cognito:User {} is created. Status: {}", email, response.user().userStatus());
            AttributeType sub = response.user().attributes().stream()
                    .filter(a ->
                            a.name().equals("sub")).findFirst()
                    .orElseThrow(() -> new RuntimeException("Missing sub from AWS response"));
            return sub.value();
        } catch (CognitoIdentityProviderException e) {
            log.error("Cognito:User creation failed for {} with msg {} ", email, e.awsErrorDetails().errorMessage());
            throw new RuntimeException("AWS User creation failed");
        }
    }
}
