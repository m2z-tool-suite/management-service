package com.m2z.tools.managementservice.aws;


import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

@ConfigurationProperties(prefix = "ms.aws")
@ConfigurationPropertiesScan
public class AwsCredentialConfig implements AwsCredentialsProvider {

    @NotBlank
    private final String accessKey;
    @NotBlank
    private final String secretKey;
    @NotBlank
    private final String region;

    private AwsCredentialConfig(String accessKey, String secretKey, String region) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = region;
    }

    @Override
    public AwsCredentials resolveCredentials() {
        return AwsBasicCredentials.create(accessKey, secretKey);
    }

    public String region() {
        return region;
    }
}
