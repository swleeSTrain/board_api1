package org.sunbong.board_api1.notice.controller;

import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/api/v1/notice")
@Log4j2
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 공지사항 목록 (페이징 포함)
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<NoticeListDTO>> list(
            @Validated PageRequestDTO pageRequestDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        PageResponseDTO<NoticeListDTO> result = noticeService.list(pageRequestDTO);
        return ResponseEntity.ok(result);
    }

    // 공지사항 등록(Create)
    @PostMapping("/add")
    public ResponseEntity<NoticeDTO> create(
            @RequestPart("notice") NoticeDTO noticeDTO,
            @RequestPart("files") List<MultipartFile> files) throws IOException {

        noticeDTO.setFiles(files);
        NoticeDTO savedNotice = noticeService.save(noticeDTO);

        return ResponseEntity.ok(savedNotice);
    }

    // 공지사항 조회 (단건)
    @GetMapping("/{noticeNo}")
    public ResponseEntity<NoticeDTO> getNotice(@PathVariable Long noticeNo) {
        NoticeDTO noticeDTO = noticeService.findById(noticeNo);
        return ResponseEntity.ok(noticeDTO);
    }

    // 공지사항 삭제
    @DeleteMapping("/{noticeNo}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeNo) {
        noticeService.delete(noticeNo);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

    // 공지사항 수정(Update)
    @PutMapping("/{noticeNo}")
    public ResponseEntity<NoticeDTO> updateNotice(
            @PathVariable Long noticeNo,
            @RequestPart("notice") NoticeDTO noticeDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {

        noticeDTO.setFiles(files);
        NoticeDTO updatedNotice = noticeService.update(noticeNo, noticeDTO);

        return ResponseEntity.ok(updatedNotice);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchNotices(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String writer,
            @Validated @ModelAttribute PageRequestDTO pageRequestDTO,
            BindingResult bindingResult) {

        // 유효성 검사 실패 시 에러 반환
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        // 검색 결과 가져오기
        PageResponseDTO<NoticeListDTO> result = noticeService.searchNotices(title, content, writer, pageRequestDTO);
        return ResponseEntity.ok(result);
    }

}

