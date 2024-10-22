package org.sunbong.board_api1.qna.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sunbong.board_api1.qna.dto.AnswerAddDTO;
import org.sunbong.board_api1.qna.dto.QuestionAddDTO;
import org.sunbong.board_api1.qna.service.AnswerService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/qna/answer")
@Log4j2
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping("add")
    public ResponseEntity<Long> registerAnswer(@RequestBody AnswerAddDTO dto) {

        Long ano = answerService.registerAnswer(dto);

        return ResponseEntity.ok(ano);
    }

    @DeleteMapping("/{ano}")
    public ResponseEntity<Long> delete(@PathVariable Long ano) {

        Long deletedAno = answerService.delete(ano);

        return ResponseEntity.ok(deletedAno);
    }

    @PutMapping("/{ano}")
    public ResponseEntity<Long> updateAnswer(
            @PathVariable Long ano,
            @RequestBody AnswerAddDTO dto) {

        Long updatedAno = answerService.updateAnswer(ano, dto);

        return ResponseEntity.ok(updatedAno);
    }
}
