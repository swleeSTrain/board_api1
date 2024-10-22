package org.sunbong.board_api1.notice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.notice.dto.NoticeDTO;
import org.sunbong.board_api1.notice.dto.NoticeListDTO;
import org.sunbong.board_api1.notice.service.NoticeService;

import java.io.IOException;
import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/notice")
@Log4j2
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<NoticeListDTO>> list(@Validated PageRequestDTO pageRequestDTO, BindingResult bindingResult) {
        // 벨리데이션 에러가 있는지 확인
        if (bindingResult.hasErrors() || pageRequestDTO.getPage() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        PageResponseDTO<NoticeListDTO> result = noticeService.list(pageRequestDTO);
        return ResponseEntity.ok(result);
    }

    // 등록
    @PostMapping("/add")
    public ResponseEntity<NoticeDTO> create(@ModelAttribute NoticeDTO noticeDTO,
                                            @RequestParam(value = "files", required = false) List<MultipartFile> files) throws IOException {

        if (files != null && !files.isEmpty()) {
            noticeDTO.setFiles(files);
        }

        NoticeDTO savedNotice = noticeService.save(noticeDTO);

        return ResponseEntity.ok(savedNotice);
    }

    // 조회
    @GetMapping("/{noticeNo}")
    public ResponseEntity<NoticeDTO> getNotice(@PathVariable Long noticeNo) {

        NoticeDTO noticeDTO = noticeService.findById(noticeNo);

        return ResponseEntity.ok(noticeDTO);
    }

    // 삭제
    @DeleteMapping("/{noticeNo}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeNo) {

        noticeService.delete(noticeNo);

        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

    // 수정
    @PutMapping("/{noticeNo}")
    public ResponseEntity<NoticeDTO> updateNotice(
            @PathVariable Long noticeNo,
            @ModelAttribute NoticeDTO noticeDTO,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) throws IOException {

        if (files != null && !files.isEmpty()) {
            noticeDTO.setFiles(files);
        }

        NoticeDTO updatedNotice = noticeService.update(noticeNo, noticeDTO);

        return ResponseEntity.ok(updatedNotice);
    }

}
