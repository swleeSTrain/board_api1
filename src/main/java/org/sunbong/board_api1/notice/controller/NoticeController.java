package org.sunbong.board_api1.notice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.common.notice.dto.NoticePageRequestDTO;
import org.sunbong.board_api1.notice.dto.NoticeDTO;
import org.sunbong.board_api1.notice.service.NoticeService;
import org.sunbong.board_api1.notice.repository.search.SearchType;

@RestController
@RequestMapping("/api/v1/notice")
@RequiredArgsConstructor
@Log4j2
public class NoticeController {

    private final NoticeService noticeService;

    // 전체 공지사항 조회 (고정 공지사항 포함)
    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<NoticeDTO>> getAllNotices(NoticePageRequestDTO requestDTO) {
        log.info("Fetching all notices with pinned ones first");
        PageResponseDTO<NoticeDTO> response = noticeService.getAllNoticesWithPinnedFirst(requestDTO);
        return ResponseEntity.ok(response);
    }

    // 검색 조건에 따라 공지사항 조회 (고정 공지사항 포함)
    @GetMapping("/search/{type}/{keyword}")
    public ResponseEntity<PageResponseDTO<NoticeDTO>> searchNotices(
            @PathVariable("type") String type,
            @PathVariable("keyword") String keyword,
            NoticePageRequestDTO requestDTO) {

        // fromString 메서드를 사용해 문자열을 SearchType으로 변환
        SearchType searchType = SearchType.fromString(type);

        log.info("Searching notices with type: {} and keyword: {}", searchType, keyword);
        PageResponseDTO<NoticeDTO> response = noticeService.searchNotices(searchType, keyword, requestDTO);
        return ResponseEntity.ok(response);
    }

    // 특정 공지사항 조회
    @GetMapping("/{nno}")
    public ResponseEntity<NoticeDTO> getNoticeById(@PathVariable("nno") Long nno) {
        try {
            NoticeDTO notice = noticeService.findById(nno);
            return ResponseEntity.ok(notice);
        } catch (Exception e) {
            log.error("Error finding notice with id: {}", nno, e);
            return ResponseEntity.notFound().build();
        }
    }

    // 공지사항 등록
    @PostMapping("add")
    public ResponseEntity<NoticeDTO> createNotice(@RequestBody NoticeDTO noticeDTO) {
        log.info("Creating new notice: {}", noticeDTO);
        NoticeDTO savedNotice = noticeService.save(noticeDTO);
        return ResponseEntity.ok(savedNotice);
    }

    // 공지사항 수정
    @PutMapping("/{nno}")
    public ResponseEntity<NoticeDTO> updateNotice(
            @PathVariable("nno") Long nno,
            @RequestBody NoticeDTO noticeDTO) {
        try {
            NoticeDTO updatedNotice = noticeService.updateNotice(nno, noticeDTO);
            return ResponseEntity.ok(updatedNotice);
        } catch (Exception e) {
            log.error("Error updating notice with id: {}", nno, e);
            return ResponseEntity.notFound().build();
        }
    }

    // 공지사항 삭제
    @DeleteMapping("/{nno}")
    public ResponseEntity<Void> deleteNotice(@PathVariable("nno") Long nno) {
        try {
            noticeService.deleteById(nno);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting notice with id: {}", nno, e);
            return ResponseEntity.notFound().build();
        }
    }
}
