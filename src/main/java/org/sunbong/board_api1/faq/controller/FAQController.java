package org.sunbong.board_api1.faq.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.faq.domain.FAQData;
import org.sunbong.board_api1.faq.dto.FAQListDTO;
import org.sunbong.board_api1.faq.dto.FAQReadDTO;
import org.sunbong.board_api1.faq.dto.FAQUpdateDTO;
import org.sunbong.board_api1.faq.service.FAQService;
import org.sunbong.board_api1.faq.dto.FAQAddDTO;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping( "/faq")
//@CrossOrigin(origins="http://10.10.10.54:13306")
public class FAQController {

    @Autowired
    private FAQService faqService;

    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<Long> faqAdd(@ModelAttribute FAQAddDTO faqDataDto) throws IOException {

        Long fno = faqService.FAQAdd(faqDataDto);
        return ResponseEntity.ok(fno);
    }

    // 예외 처리
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<String> handleMultipartException(MultipartException ex) {
        return new ResponseEntity<>("File upload error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete/{fno}")
    public ResponseEntity<String> deleteFaq(@PathVariable Long fno) {
        faqService.deleteByFno(fno);
        return ResponseEntity.ok("삭제완료");
    }

    @GetMapping("/read/{fno}")
    public ResponseEntity<FAQReadDTO> readFaq(@PathVariable Long fno) {
        FAQData faqData = faqService.ReadByFno(fno);
        FAQReadDTO faqReadDTO = FAQReadDTO.builder()
                .question(faqData.getQuestion())
                .answer(faqData.getAnswer())
                .classificationType(faqData.getClassificationType())
                .build();
        return ResponseEntity.ok(faqReadDTO);
    }

    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO> getFaqList(@Validated PageRequestDTO requestDto) throws IOException {

        return ResponseEntity.ok(faqService.list(requestDto));
    }

    @PutMapping("/{fno}")
    public ResponseEntity<Long> updateFAQ(@PathVariable Long fno, @ModelAttribute FAQUpdateDTO faqUpdateDto) throws IOException {
        Long fno1 = faqService.updateFAQ(fno, faqUpdateDto);

        return ResponseEntity.ok(fno1);
    }






}

