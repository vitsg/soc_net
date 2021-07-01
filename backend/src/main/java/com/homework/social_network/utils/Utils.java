package com.homework.social_network.utils;

import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.Photo;
import org.apache.commons.io.IOUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.InputStream;

import static java.util.Objects.requireNonNull;

public class Utils {

    public static void setPrincipal(Account account) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(account, currentAuth.getCredentials(),
                account.getAuthorities());
        auth.setDetails(currentAuth.getDetails());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public static byte[] getDefaultImage() {
        try {
            InputStream is = Utils.class.getClassLoader().getResourceAsStream("defaultImage.jpg");
            return IOUtils.toByteArray(requireNonNull(is));
        } catch (Exception e) {
            throw new RuntimeException("Невозможно получить дефолтное фото", e);
        }
    }

    public static byte[] getOrDefaultContentPhoto(Photo photo) {
        if (photo == null) {
            return getDefaultImage();
        } else {
            return photo.getData();
        }
    }
}
