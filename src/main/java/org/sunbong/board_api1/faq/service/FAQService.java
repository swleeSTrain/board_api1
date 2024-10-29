package org.sunbong.board_api1.faq.service;


import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.common.exception.CommonExceptions;
import org.sunbong.board_api1.common.util.FileUtil;
import org.sunbong.board_api1.faq.domain.FAQData;
import org.sunbong.board_api1.faq.dto.FAQListDTO;
import org.sunbong.board_api1.faq.dto.FAQAddDTO;
import org.sunbong.board_api1.faq.dto.FAQUpdateDTO;
import org.sunbong.board_api1.faq.repository.FAQRepository;
import org.sunbong.board_api1.faq.search.FAQSearch;
import org.sunbong.board_api1.faq.search.FAQSearchImpl;

import java.io.IOException;

@Service
@Transactional
@Log4j2
public class FAQService {

    @Autowired
    private  FAQRepository faqRepository;
    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private FAQSearch faqSearch;

    public Long addFaq( FAQAddDTO faqDataAddDto) throws IOException {

        // FAQ 엔티티 생성
        FAQData faqData = FAQData.builder()
                .question(faqDataAddDto.getQuestion())
                .answer(faqDataAddDto.getAnswer())
                .classificationType(faqDataAddDto.getClassificationType())
                .build();

        // 업로드할 파일이 있을 경우
        if (faqDataAddDto.getFiles() != null && !faqDataAddDto.getFiles().isEmpty()) {
            for (MultipartFile file : faqDataAddDto.getFiles()) {
                // 파일 저장 후 저장된 파일명을 question에 추가
                String savedFileName = fileUtil.saveFile(file);
                faqData.addFile(savedFileName);  // 저장된 파일명을 추가
            }
        }

        FAQData savedFaqData = faqRepository.save(faqData);

        return savedFaqData.getFno();

    }

    public void deleteByFno(Long fno){
        faqRepository.deleteById(fno);
    }

    public void modifyByFno(Long fno, FAQData faqData){

    }

    public PageResponseDTO<FAQListDTO> list (PageRequestDTO pageRequestDTO){
        if(pageRequestDTO.getPage() < 0 ){
            throw CommonExceptions.LIST_ERROR.get();
        }
        return faqRepository.listByFno(1L, pageRequestDTO);
    }

    public FAQData readByFno (Long fno){

        return faqRepository.findById(fno).orElse(null);

    }

    public PageResponseDTO<FAQListDTO> faqList (PageRequestDTO pageRequestDTO){

        if (pageRequestDTO.getPage() < 0 ){
            throw CommonExceptions.LIST_ERROR.get();
        }
        PageResponseDTO<FAQListDTO> result = faqRepository.listByFno(1L, pageRequestDTO);

        return result;
    }

    public Long updateFAQ(@PathVariable Long qno, @ModelAttribute FAQUpdateDTO dto) throws IOException {
        FAQData faqData = faqRepository.findById(qno).orElseThrow(() -> new IllegalArgumentException("Invalid question ID"));

        FAQData FaqData = faqData.toBuilder()
                .classificationType(dto.getClassification())
                .question(dto.getQuestion())
                .answer(dto.getAnswer())
                .build();

        // 기존 파일 삭제
        if (dto.getAttachFiles() != null && !dto.getAttachFiles().isEmpty()) {
            for (String fileName : dto.getAttachFiles()) {
                fileUtil.deleteFile(fileName); // 여기에서 QnaService의 deleteFile 메서드 호출
            }
        }

        // 새로운 파일 추가
        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            for (MultipartFile file : dto.getFiles()) {
                String savedFileName = fileUtil.saveFile(file);
                FaqData.addFile(savedFileName); // 새로운 파일 추가
            }
        }

        // 질문 업데이트
        faqRepository.save(faqData);

        return faqData.getFno(); // 업데이트된 질문의 ID 반환

    }






}
