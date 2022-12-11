package com.m2z.tools.managementservice.aws;


import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;

@ConfigurationProperties(prefix = "ms.aws")
@ConfigurationPropertiesScan
public class AwsCredentialConfig implements AwsCredentialsProvider {

    @NotBlank
    private final String accessKey;
    @NotBlank
    private final String secretKey;
    @NotBlank
    private final Region region;

    private AwsCredentialConfig(String accessKey, String secretKey, String region) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = Region.of(region);
    }

    @Override
    public AwsCredentials resolveCredentials() {
        return AwsBasicCredentials.create(accessKey, secretKey);
    }

    public Region region() {
        return region;
    }
}
