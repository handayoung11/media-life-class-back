package com.jin.miracle.util;

import com.jin.miracle.file.FileType;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static com.jin.miracle.file.FileType.BASE_DIR;
import static org.apache.commons.io.FilenameUtils.getExtension;

@Slf4j
public class FileUtils {

    private static final String UPLOAD_DIR;

    static {
        try {
            UPLOAD_DIR = new File(BASE_DIR.getName()).getCanonicalPath();
            Files.createDirectories(Path.of(UPLOAD_DIR)); // 없으면 생성
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String saveImgByUrl(String imageUrl, String name) {
        try {
            Path dirPath = Paths.get(UPLOAD_DIR, FileType.USER_DIR.getName());
            Files.createDirectories(dirPath);

            String extension = getExtension(imageUrl);
            if (extension.isEmpty()) extension = "jpg";
            String fileName = name + "." + extension;
            Path savePath = dirPath.resolve(fileName);

            // 이미지 다운로드 및 저장
            try (InputStream in = new URL(imageUrl).openStream()) {
                Files.copy(in, savePath, StandardCopyOption.REPLACE_EXISTING);
            }

            return fileName;
        } catch (IOException e) {
            log.error("이미지 저장 중 오류 발생: {}", e.getMessage());
        }

        return null;
    }
}
