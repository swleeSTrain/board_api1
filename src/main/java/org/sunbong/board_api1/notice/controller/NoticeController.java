package org.sunbong.board_api1.notice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.notice.dto.NoticeListDTO;
import org.sunbong.board_api1.notice.service.NoticeService;

@RestController
@RequestMapping("/api/v1/notice")
@Log4j2
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;
    @GetMapping("nlist")
    public ResponseEntity<PageResponseDTO<NoticeListDTO>> list(PageRequestDTO pageRequestDTO) {
        PageResponseDTO<NoticeListDTO> result = noticeService.list(pageRequestDTO);
        return ResponseEntity.ok(result);
    }

}
