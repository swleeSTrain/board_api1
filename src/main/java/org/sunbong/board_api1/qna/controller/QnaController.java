package org.sunbong.board_api1.qna.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.product.dto.ProductListDTO;
import org.sunbong.board_api1.product.service.ProductService;
import org.sunbong.board_api1.qna.dto.QnaListDTO;
import org.sunbong.board_api1.qna.dto.QnaRegisterDTO;
import org.sunbong.board_api1.qna.service.QnaService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/qna")
@Log4j2
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;

    @GetMapping("list")
    public ResponseEntity<Page<QnaListDTO>> list(
            @Validated PageRequestDTO requestDTO
    ) {
        log.info("--------------------------Qna Controller list");
        log.info("==============================");
        return ResponseEntity.ok(qnaService.list(requestDTO));
    }

    @PostMapping("register")
    public ResponseEntity<String> register(
            @Validated @RequestBody QnaRegisterDTO qnaRegisterDTO
            ) {
        log.info("Registering new QnA: " + qnaRegisterDTO);
        qnaService.register(qnaRegisterDTO);
        return ResponseEntity.ok("QnA registered successfully");
    }


}