package com.fivesysdev.Fiveogram.util;

import com.fivesysdev.Fiveogram.config.MyUserDetails;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class Context {
    public static User getUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((MyUserDetails) authentication.getPrincipal()).getUser();
    }
}
