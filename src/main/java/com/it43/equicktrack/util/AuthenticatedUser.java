package com.it43.equicktrack.util;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUser {
    public static Object getAuthenticatedUser(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
