package org.sunbong.board_api1.faq.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.sunbong.board_api1.faq.domain.FAQData;
import org.sunbong.board_api1.faq.dto.FAQDto;
import org.sunbong.board_api1.faq.repository.FAQRepository;

@Controller
@RequestMapping( "/faq")
//@CrossOrigin(origins="http://10.10.10.54:13306")
public class FAQController {


    @Autowired
    FAQRepository faqRepository;

    @PostMapping("/add")
    public ResponseEntity<String> faqAdd(@RequestBody FAQDto faqDto){
        FAQData faqData = FAQData.builder()
                .writer(faqDto.getWriter())
                .answer(faqDto.getAnswer())
                .build();

        faqRepository.save(faqData);
       return new ResponseEntity<>("add success", HttpStatus.OK);
    }





}
