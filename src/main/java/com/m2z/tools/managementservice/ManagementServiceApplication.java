package com.m2z.tools.managementservice;

import com.m2z.tools.managementservice.aws.AwsCredentialConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({AwsCredentialConfig.class})
public class ManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagementServiceApplication.class, args);
    }

}
