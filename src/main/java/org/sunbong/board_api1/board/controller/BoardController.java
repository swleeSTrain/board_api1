package org.sunbong.board_api1.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sunbong.board_api1.board.dto.BoardPostAddDTO;
import org.sunbong.board_api1.board.dto.BoardPostListDTO;
import org.sunbong.board_api1.board.dto.BoardPostReadDTO;
import org.sunbong.board_api1.board.service.BoardPostService;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/board")
@Log4j2
@RequiredArgsConstructor
//@PreAuthorize("true")//처음엔 여기다 집어넣기
public class BoardController {

    private final BoardPostService boardPostService;
    //페이지읽기
    @GetMapping("{bno}")
    public ResponseEntity<PageResponseDTO<BoardPostReadDTO>> readPost(
            @PathVariable("bno") Long bno,
            @Validated PageRequestDTO pageRequestDTO) {
        log.info("readByBno {}", bno);
        return ResponseEntity.ok(boardPostService.readByBno(bno, pageRequestDTO));
    }
    //리스트 목록 출력
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<BoardPostListDTO>> listPost(
            @Validated PageRequestDTO pageRequestDTO){
        return ResponseEntity.ok(boardPostService.listByBno(pageRequestDTO));
    }

    // POST 요청으로 게시글 삽입
    @PostMapping("/add")
    public ResponseEntity<Long> newPost(
            @ModelAttribute BoardPostAddDTO dto,
            @RequestParam("files")List<MultipartFile> files) {
        // 서비스 계층에서 게시글을 저장
        Long bno = boardPostService.newPost(dto, files);
        return ResponseEntity.ok(bno);  // 생성된 게시글의 번호를 응답
    }

    //Delete 요청으로 delflag 소프트삭제
    @DeleteMapping("{bno}")
    public ResponseEntity<Long> deletePost(@PathVariable("bno") Long bno) {
        // 소프트 삭제 처리
        boardPostService.softDeletePost(bno);
        return ResponseEntity.noContent().build();
    }

    //Put 요청으로 수정
    @PutMapping("{bno}")
    public ResponseEntity<Long> updatePost(@PathVariable("bno") Long bno,
                                           @ModelAttribute BoardPostAddDTO dto,
                                           @RequestParam("files")List<MultipartFile> files) {
        boardPostService.updatePost(bno,dto,files);
        return ResponseEntity.ok(dto.getBno());
    }
}
