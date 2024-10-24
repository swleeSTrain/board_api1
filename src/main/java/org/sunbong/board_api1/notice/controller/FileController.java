package org.sunbong.board_api1.notice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.sunbong.board_api1.notice.service.FileService;

import java.nio.file.Path;
//도저히 모르겠습니다 죄송합니다.
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/download/{fileName}")
    public ResponseEntity<FileSystemResource> downloadFile(@PathVariable String fileName) {
        try {
            Path filePath = fileService.downloadFile(fileName);
            FileSystemResource resource = new FileSystemResource(filePath.toFile());

            HttpHeaders headers = new HttpHeaders();

            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + extractOriginalFileName(fileName) + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream"); // 일반 파일 다운로드

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 원래 파일 이름을 추출하는 메서드
    private String extractOriginalFileName(String fileName) {

        // UUID가 포함된 파일 이름에서 원래 파일 이름을 반환
        String[] parts = fileName.split("_");
        if (parts.length > 1) {
            return parts[1];
        }
        return fileName;
    }
}
