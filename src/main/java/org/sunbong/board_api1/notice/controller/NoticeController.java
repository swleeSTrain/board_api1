package org.sunbong.board_api1.notice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<NoticeListDTO>> list(@Valid PageRequestDTO pageRequestDTO, BindingResult bindingResult) {
        // 벨리데이션 에러가 있는지 확인
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        PageResponseDTO<NoticeListDTO> result = noticeService.list(pageRequestDTO);
        return ResponseEntity.ok(result);
    }

    // 등록(Create)
    @PostMapping("/add")
    public ResponseEntity<NoticeDTO> create(@RequestPart("notice") NoticeDTO noticeDTO,
                                            @RequestPart("files") List<MultipartFile> files) throws IOException {

        // NoticeDTO에 파일 설정
        noticeDTO.setFiles(files);

        // 공지사항 저장
        NoticeDTO savedNotice = noticeService.save(noticeDTO);

        return ResponseEntity.ok(savedNotice);
    }

    @GetMapping("/{noticeNo}")
    public ResponseEntity<NoticeDTO> getNotice(@PathVariable Long id) {

        NoticeDTO noticeDTO = noticeService.findById(id);

        return ResponseEntity.ok(noticeDTO);
    }

    @DeleteMapping("/{noticeNo}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {

        noticeService.delete(id);

        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

    // 수정
    @PutMapping("/{noticeNo}")
    public ResponseEntity<NoticeDTO> updateNotice(
            @PathVariable Long noticeNo,
            @RequestPart("notice") NoticeDTO noticeDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {

        noticeDTO.setFiles(files);

        NoticeDTO updatedNotice = noticeService.update(noticeNo, noticeDTO);

        return ResponseEntity.ok(updatedNotice);
    }


}
