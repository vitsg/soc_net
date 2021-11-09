package com.homework.social_network.utils;

import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.Photo;
import com.homework.social_network.enums.ErrorCode;
import com.homework.social_network.exception.CustomException;
import org.apache.commons.io.IOUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class Utils {

    private static final long MAX_PHOTO_SIZE = 1024 * 1024 * 10;

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

    public static Account getCurrentAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Account) auth.getPrincipal();
    }

    public static UUID getCurrentAccountId() {
        return getCurrentAccount().getId();
    }

    public static LocalDateTime getCurrentDateTime() {
        return OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime();
    }

    public static void validatePhotoSize(MultipartFile file) {
        if (file == null) {
            return;
        }
        if (file.getSize() > MAX_PHOTO_SIZE) {
            throw new CustomException(ErrorCode.PHOTO_SIZE_OVERFLOW);
        }
    }

    public static void validatePhotosSize(List<MultipartFile> files) {
        Optional.ofNullable(files)
                .orElse(Collections.emptyList())
                .forEach(Utils::validatePhotoSize);
    }
}
