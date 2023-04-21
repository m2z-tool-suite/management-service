package com.m2z.tools.managementservice.security.service;

import com.m2z.tools.managementservice.security.exception.BadTokenException;
import com.m2z.tools.managementservice.security.exception.InternalTokenProcessingException;
import com.m2z.tools.managementservice.security.model.PrincipleUser;

public interface JwkService {

    PrincipleUser process(final String idToken) throws BadTokenException, InternalTokenProcessingException;
}
