package org.sunbong.board_api1.notice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class FileService {

    @Value("${org.sunbong.upload.path}")
    private String uploadDir;

    // 파일 저장 메서드
    public String saveFile(MultipartFile file) throws IOException {
        try {
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);
            return fileName;
        } catch (IOException e) {
            log.error("파일 저장 중 오류 발생: " + file.getOriginalFilename(), e);
            throw new IOException("파일 저장 실패", e);
        }
    }

    // 파일 삭제 메서드
    public void deleteFile(String fileName) {
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("파일 삭제 중 오류 발생: " + fileName, e);
        }
    }

    // 파일 다운로드 메서드
    public Path downloadFile(String fileName) {
        Path filePath = Paths.get(uploadDir).resolve(fileName);

        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }

        return filePath;
    }
}
