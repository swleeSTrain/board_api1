package org.sunbong.board_api1.qna.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.qna.dto.AnswerRegisterDTO;
import org.sunbong.board_api1.qna.dto.QuestionListDTO;
import org.sunbong.board_api1.qna.dto.QuestionAddDTO;
import org.sunbong.board_api1.qna.service.QnaService;

@RestController
@RequestMapping("/api/v1/qna")
@Log4j2
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;

    @GetMapping("list")
    public ResponseEntity<Page<QuestionListDTO>> list(
            @Validated PageRequestDTO requestDTO
    ) {

        log.info("--------------------------Qna Controller list");
        log.info("==============================");

        return ResponseEntity.ok(qnaService.list(requestDTO));
    }

    @PostMapping("add/question")
    public ResponseEntity<Long> registerQuestion(@RequestBody QuestionAddDTO dto) {

        Long qno = qnaService.registerQuestion(dto);

        return ResponseEntity.ok(qno);
    }

    @PostMapping("add/answer")
    public ResponseEntity<Long> registerAnswer(@RequestBody AnswerRegisterDTO dto) {

        Long ano = qnaService.registerAnswer(dto);

        return ResponseEntity.ok(ano);
    }



}