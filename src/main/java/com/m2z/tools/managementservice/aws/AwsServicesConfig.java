package com.m2z.tools.managementservice.aws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
public class AwsServicesConfig {

    private final AwsCredentialConfig credentialConfig;

    public AwsServicesConfig(AwsCredentialConfig credentialConfig) {
        this.credentialConfig = credentialConfig;
    }

    @Bean
    public CognitoIdentityProviderClient cognitoIdentityProviderClientBean() {
        return CognitoIdentityProviderClient.builder().credentialsProvider(credentialConfig).build();
    }
}
