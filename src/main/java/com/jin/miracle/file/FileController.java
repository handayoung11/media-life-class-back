package com.jin.miracle.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.jin.miracle.file.FileType.BASE_DIR;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @GetMapping("/{type}/{filename}")
    public ResponseEntity<Resource> getFile(
            @PathVariable FileType type,
            @PathVariable String filename
    ) {
        try {
            Path path = Paths.get(BASE_DIR.getName(), type.getName());
            Path filePath = path.resolve(filename).normalize();

            // 보안: 경로 탈출 방지
            if (!filePath.startsWith(path.normalize())) {
                return ResponseEntity.badRequest().build();
            }

            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaTypeFactory.getMediaType(resource)
                            .orElse(MediaType.APPLICATION_OCTET_STREAM))
                    .body(resource);

        } catch (Exception e) {
            log.error("에러 발생!", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
