package com.homework.social_network.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.exif.ExifIFD0Directory;
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
import java.util.Objects;
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
        try {
            Scalr.Rotation rotation;
            try (InputStream is = file.getInputStream()) {
                rotation = Optional.ofNullable(ImageMetadataReader.readMetadata(is))
                        .map(m -> m.getFirstDirectoryOfType(ExifIFD0Directory.class))
                        .map(d -> {
                            try {
                                return d.getInt(ExifIFD0Directory.TAG_ORIENTATION);
                            } catch (Exception e) {
                                return null;
                            }
                        })
                        .map(orientation -> {
                            switch (orientation) {
                                case 6:
                                    return Scalr.Rotation.CW_90;
                                case 3:
                                    return Scalr.Rotation.CW_180;
                                case 8:
                                    return Scalr.Rotation.CW_270;
                                case 1:
                                default:
                                    return null;
                            }
                        })
                        .orElse(null);
            }

            try (InputStream is = file.getInputStream()) {
                BufferedImage bi = ImageIO.read(is);
                if (Objects.nonNull(rotation)) {
                    return Scalr.rotate(bi, rotation);
                }
                return bi;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get BufferedImage " + file.getOriginalFilename());
        }
    }
}
