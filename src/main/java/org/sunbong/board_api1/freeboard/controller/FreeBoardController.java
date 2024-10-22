package org.sunbong.board_api1.freeboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.sunbong.board_api1.freeboard.dto.FreeBoardPostListDTO;
import org.sunbong.board_api1.freeboard.service.FreeBoardPostService;

@RestController
@RequestMapping("/api/v1/freeboard")
@Log4j2
@RequiredArgsConstructor
//@PreAuthorize("true")//처음엔 여기다 집어넣기
public class FreeBoardController {

    private final FreeBoardPostService freeBoardPostService;

    // POST 요청으로 게시글 삽입
    @PostMapping("/post")
    public ResponseEntity<Long> newPost(@RequestBody FreeBoardPostListDTO dto) {
        // 서비스 계층에서 게시글을 저장
        Long bno = freeBoardPostService.newPost(dto);
        return ResponseEntity.ok(bno);  // 생성된 게시글의 번호를 응답
    }
    @DeleteMapping("/delete/{bno}")
    public ResponseEntity<Long> deletePost(@PathVariable("bno") Long bno) {
        // 소프트 삭제 처리
        freeBoardPostService.softDeletePost(bno);
        return ResponseEntity.noContent().build();
    }
}
