package com.example.demo.chat.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

public class ImageUtils {

    public static String convertUrlToBase64String(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream inputStream = url.openStream()) {
            byte[] bytes = inputStream.readAllBytes();
            return Base64.getEncoder().encodeToString(bytes);
        }
    }

    public static String getMimeTypeFromUrl(String imageUrl) {
        String extension = getFileExtensionFromUrl(imageUrl);
        return switch (extension.toLowerCase()) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            default -> "image/jpeg"; // default fallback
        };
    }

    public static String getFileExtensionFromUrl(String imageUrl) {
        int lastDot = imageUrl.lastIndexOf('.');
        int lastSlash = imageUrl.lastIndexOf('/');
        int queryStart = imageUrl.indexOf('?');

        if (lastDot > lastSlash && (queryStart == -1 || lastDot < queryStart)) {
            String extension = imageUrl.substring(lastDot + 1);
            if (queryStart != -1 && lastDot < queryStart) {
                extension = extension.substring(0, queryStart - lastDot - 1);
            }
            return extension;
        }
        return "jpg"; // default fallback
    }

    public static boolean isValidImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return false;
        }

        try {
            new URL(imageUrl); // Validate URL format
            String extension = getFileExtensionFromUrl(imageUrl);
            return isValidImageExtension(extension);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidImageExtension(String extension) {
        return extension != null &&
               (extension.toLowerCase().matches("jpg|jpeg|png|gif|webp"));
    }
}
