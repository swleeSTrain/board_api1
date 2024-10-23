package org.sunbong.board_api1.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.sunbong.board_api1.board.dto.CommentDTO;
import org.sunbong.board_api1.board.dto.CommentListDTO;
import org.sunbong.board_api1.board.service.CommentService;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;

@RestController
@RequestMapping("/api/v1/comment")
@Log4j2
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @GetMapping("board/{bno}")
    public ResponseEntity<PageResponseDTO<CommentListDTO>> readComment(
            @PathVariable("bno") Long bno,
            @Validated PageRequestDTO pageRequestDTO) {
        log.info("readByBno {}", bno);
        return ResponseEntity.ok(commentService.readByBno(bno, pageRequestDTO));
    }

    @PostMapping("add")
    public ResponseEntity<Long> newComment(
            @ModelAttribute CommentDTO CommentDTO) {

        Long cno = commentService.newComment(CommentDTO);
        return ResponseEntity.ok(cno);
    }
    @PutMapping("{cno}")
    public ResponseEntity<Long> updateComment(
            @PathVariable Long cno,
            @ModelAttribute CommentDTO CommentDTO
    ){
      commentService.updateComment(cno,CommentDTO);
      return ResponseEntity.ok(CommentDTO.getCno());
    }
    @DeleteMapping("{cno}")
    public ResponseEntity<Long> deleteComment(
            @PathVariable Long cno
    ){
        commentService.deleteComment(cno);
        return ResponseEntity.ok(cno);
    }
}
