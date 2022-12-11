package com.m2z.tools.managementservice.aws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsServicesConfig {

    private final AwsCredentialConfig credentialConfig;

    public AwsServicesConfig(AwsCredentialConfig credentialConfig) {
        this.credentialConfig = credentialConfig;
    }

    @Bean
    public CognitoIdentityProviderClient cognitoIdentityProviderClientBean() {
        return CognitoIdentityProviderClient.builder()
                .credentialsProvider(credentialConfig)
                .build();
    }
    @Bean
    public S3Client s3ClientBean() {
        return S3Client.builder()
                .credentialsProvider(credentialConfig)
                .region(credentialConfig.region())
                .build();
    }

    @Bean
    public S3Presigner s3PresignerBean() {
        return S3Presigner.builder()
                .credentialsProvider(credentialConfig)
                .region(credentialConfig.region())
                .build();
    }
}
