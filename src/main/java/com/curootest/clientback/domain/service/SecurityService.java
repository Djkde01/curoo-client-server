package com.curootest.clientback.domain.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Service for handling security-related operations in the domain layer
 */
@Service
public class SecurityService {

    /**
     * Gets the email of the currently authenticated user from the JWT token
     * 
     * @return the email of the authenticated user
     * @throws RuntimeException if no user is authenticated
     */
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }

        throw new RuntimeException("Unable to extract user email from authentication context");
    }

    /**
     * Checks if there is a currently authenticated user
     * 
     * @return true if there is an authenticated user, false otherwise
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal());
    }
}
