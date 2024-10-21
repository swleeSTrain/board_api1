package org.sunbong.board_api1.qna.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.common.exception.CommonExceptions;
import org.sunbong.board_api1.product.repository.ProductRepository;
import org.sunbong.board_api1.qna.dto.QnaListDTO;
import org.sunbong.board_api1.qna.repository.QnaRepository;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;

    public Page<QnaListDTO> list(PageRequestDTO pageRequestDTO) {

        // 페이지 번호가 0보다 작으면 예외 발생
        if (pageRequestDTO.getPage() < 0) {
            throw CommonExceptions.LIST_ERROR.get();
        }

        // PageRequestDTO에서 Pageable 객체 생성
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

        // QnaRepository의 list 메서드를 호출하여 페이징 결과 얻음
        return qnaRepository.list(pageable);
    }
}