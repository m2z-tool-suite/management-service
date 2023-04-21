package com.m2z.tools.managementservice.security.dto;

import java.util.Set;

public record UserAuthoritiesResponse(Set<String> authorities) {
}
