package org.sunbong.board_api1.notice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.common.exception.CommonExceptions;
import org.sunbong.board_api1.notice.dto.NoticeListDTO;
import org.sunbong.board_api1.notice.repository.NoticeRepository;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public PageResponseDTO<NoticeListDTO> list(PageRequestDTO pageRequestDTO) {

        log.info("Fetching notice list with pagination");

        if(pageRequestDTO.getPage() < 0) {
            throw CommonExceptions.LIST_ERROR.get();
        }
        // NoticeSearch 인터페이스를 통해 공지사항 목록을 조회
        PageResponseDTO<NoticeListDTO> result = noticeRepository.noticeList(pageRequestDTO);

        return result;
    }
}
