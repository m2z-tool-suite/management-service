package com.m2z.tools.managementservice.security.config;

import com.m2z.tools.managementservice.aws.AwsCredentialConfig;
import com.m2z.tools.managementservice.security.filters.CognitoAuthenticationFilter;
import com.m2z.tools.managementservice.security.service.JwkService;
import com.m2z.tools.managementservice.security.service.JwkServiceNimbusImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean(name = "cognitoJwkServiceImpl")
    public JwkService jwkServiceCognito(AwsCredentialConfig config, @Value("${ms.aws.cognito.userPoolId}") String userPoolId) {
        final String jwksUrl = String.format("https://cognito-idp.%s.amazonaws.com/%s%s", config.region().id(), userPoolId, "/.well-known/jwks.json");
        HashSet<String> requiredClaims = new HashSet<String>(Arrays.asList("sub", "iat", "exp", "jti", "scope", "username", "token_use")); //cognito:groups
        return new JwkServiceNimbusImpl(jwksUrl, requiredClaims,"RS256", null);
    }

    @Bean
    public SecurityFilterChain filterConfig(HttpSecurity http, JwkService jwkService) throws Exception {
        return http
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests().anyRequest().authenticated().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new CognitoAuthenticationFilter(jwkService), BasicAuthenticationFilter.class)
                .build();
    }
}
