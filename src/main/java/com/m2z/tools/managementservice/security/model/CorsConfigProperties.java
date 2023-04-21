package com.m2z.tools.managementservice.security.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationProperties(prefix = "ms.cors")
@ConfigurationPropertiesScan
public record CorsConfigProperties (String[] origins) {
}
