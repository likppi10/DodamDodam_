package com.ssafy.core.common;

import org.springframework.http.ContentDisposition;

import java.nio.charset.StandardCharsets;

public final class FileUtil {
    private static final String CATEGORY_PREFIX = "/";
    private static final String TIME_SEPARATOR = "_";
    private static final int UNDER_BAR_INDEX = 1;
    private static final String FILE_EXTENSION_SEPARATOR = ".";
    public static final int FILE_MAX_SIZE = 1048576;

    public static ContentDisposition createContentDisposition(String categoryWithFileName) {
        String fileName = categoryWithFileName.substring(
                categoryWithFileName.lastIndexOf(CATEGORY_PREFIX) + UNDER_BAR_INDEX);
        return ContentDisposition.builder("attachment")
                .filename(fileName, StandardCharsets.UTF_8)
                .build();
    }

    public static String buildFileName(String category, String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String now = String.valueOf(System.currentTimeMillis());

        return category + CATEGORY_PREFIX + fileName + TIME_SEPARATOR + now + fileExtension;
    }

    public static String buildResizedFileName(String category, String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String now = String.valueOf(System.currentTimeMillis());

        return category + CATEGORY_PREFIX + fileName + TIME_SEPARATOR + "Resized" + TIME_SEPARATOR + now + fileExtension;
    }
}
