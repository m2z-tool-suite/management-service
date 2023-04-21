package com.m2z.tools.managementservice;

import com.m2z.tools.managementservice.aws.AwsCredentialConfig;
import com.m2z.tools.managementservice.security.model.CorsConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class})
@EnableConfigurationProperties({AwsCredentialConfig.class, CorsConfigProperties.class})
public class ManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagementServiceApplication.class, args);
    }

}
