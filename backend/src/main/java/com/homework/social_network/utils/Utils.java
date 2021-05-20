package com.homework.social_network.utils;

import com.homework.social_network.entity.Account;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {

    public static void setPrincipal(Account account) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(account, currentAuth.getCredentials(),
                account.getAuthorities());
        auth.setDetails(currentAuth.getDetails());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
