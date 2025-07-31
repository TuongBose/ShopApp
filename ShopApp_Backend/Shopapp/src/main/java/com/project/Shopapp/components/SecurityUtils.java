package com.project.Shopapp.components;

import com.project.Shopapp.models.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    public Account getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
                authentication.getPrincipal() instanceof Account selectedUser) {
            if(!selectedUser.isIS_ACTIVE()) {
                return null;
            }
            return (Account) authentication.getPrincipal();
        }
        return null;
    }
}
