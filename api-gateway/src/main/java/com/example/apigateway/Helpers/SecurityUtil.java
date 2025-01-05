package com.example.apigateway.Helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtil {
    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || ! (authentication.getPrincipal() instanceof Jwt jwt) )
            return null;

        return jwt.getClaimAsString("sub");
    }
}
