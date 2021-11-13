package com.homework.social_network.utils;

import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.Photo;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class Utils {

    public static final int PORTRET_IMAGE_SIZE = 370;
    public static final int OTHER_IMAGE_SIZE = 1300;

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

    public static byte[] getImageContent(MultipartFile image, int compressImageSize) {
        String fileName = Optional.ofNullable(image.getOriginalFilename())
                .filter(StringUtils::isNotEmpty)
                .orElseThrow(() -> new RuntimeException("Empty file name"));
        BufferedImage bufferedImage = getBufferedImage(image);
        int origHeight = bufferedImage.getHeight();
        int origWidth = bufferedImage.getWidth();
        String extension = Optional.ofNullable(FilenameUtils.getExtension(fileName))
                .filter(StringUtils::isNotEmpty)
                .map(String::toLowerCase)
                .orElseThrow(() -> new RuntimeException(String.format("Define extension of file %s failed", image.getOriginalFilename())));

        if (origHeight <= compressImageSize || origWidth <= compressImageSize) {
            return bufferedImageToBytes(bufferedImage, extension, fileName);
        }
        BufferedImage compressedBufferedImage = Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, calcMode(origHeight, origWidth), compressImageSize);
        return bufferedImageToBytes(compressedBufferedImage, extension, fileName);
    }

    private static Scalr.Mode calcMode(int origHeight, int origWidth) {
        if (origHeight > origWidth) {
            return Scalr.Mode.FIT_TO_WIDTH;
        }
        return Scalr.Mode.FIT_TO_HEIGHT;
    }

    private static byte[] bufferedImageToBytes(BufferedImage bufferedImage, String extension, String fileName) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, extension, baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to write content BufferedImage " + fileName);
        }
    }

    private static BufferedImage getBufferedImage(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            return ImageIO.read(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get BufferedImage " + file.getOriginalFilename());
        }
    }
}
