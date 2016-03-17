package com.socnet.utility;

import com.socnet.Application;
import com.socnet.authentication.CurrentUser;
import com.socnet.entity.User;
import com.socnet.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUtils {

    private static UserService userService;

    public static User getCurrentAuthUser() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication authentication = ctx.getAuthentication();
        if (userService == null) {
            userService = Application.getBean(UserService.class);
        }
        return userService.findUserByEmail(((CurrentUser) authentication.getPrincipal()).getEmail());
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}
