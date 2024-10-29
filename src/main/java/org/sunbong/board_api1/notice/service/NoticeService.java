package org.sunbong.board_api1.notice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.board_api1.notice.dto.NoticePageResponseDTO;
import org.sunbong.board_api1.notice.dto.NoticePageRequestDTO;
import org.sunbong.board_api1.notice.domain.Notice;
import org.sunbong.board_api1.notice.dto.NoticeDTO;
import org.sunbong.board_api1.notice.repository.NoticeRepository;
import org.sunbong.board_api1.common.util.search.SearchType;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class NoticeService {

    private final NoticeRepository noticeRepository;

    // 고정 공지사항 포함 전체 조회 메서드
    public NoticePageResponseDTO<NoticeDTO> getAllNoticesWithPinnedFirst(NoticePageRequestDTO requestDTO) {
        // SearchType이 null일 경우 기본값 설정
        SearchType searchType = requestDTO.getSearchType() != null ? requestDTO.getSearchType() : SearchType.TITLE_WRITER_CONTENT;

        // repository 메서드가 요구하는 SearchType enum 타입을 그대로 전달
        return noticeRepository.getNoticesWithPinnedFirst(requestDTO, searchType, requestDTO.getKeyword());
    }

    // 검색 조건에 따라 공지사항 조회 (고정 공지사항 포함)
    public NoticePageResponseDTO<NoticeDTO> searchNotices(SearchType searchType, String keyword, NoticePageRequestDTO requestDTO) {
        // 만약 searchType이 null이면 기본 검색 타입 설정
        if (searchType == null) {
            searchType = SearchType.TITLE_WRITER_CONTENT;
        }

        return noticeRepository.getNoticesWithPinnedFirst(requestDTO, searchType, keyword);
    }

    // 공지사항 등록
    public NoticeDTO save(NoticeDTO noticeDTO) {
        return toDTO(noticeRepository.save(noticeDTO.toEntity()));
    }

    // 특정 공지사항 조회
    public NoticeDTO findById(Long nno) throws Exception {
        return toDTO(noticeRepository.findById(nno)
                .orElseThrow(() -> new Exception("Notice with ID " + nno + " not found.")));
    }

    // 공지사항 삭제
    public void deleteById(Long nno) throws Exception {
        Notice notice = noticeRepository.findById(nno)
                .orElseThrow(() -> new Exception("Notice with ID " + nno + " not found."));
        noticeRepository.delete(notice);
    }

    // 공지사항 수정 (변경 감지 사용)
    public NoticeDTO updateNotice(Long nno, NoticeDTO updatedNoticeDTO) throws Exception {
        Notice notice = noticeRepository.findById(nno)
                .orElseThrow(() -> new Exception("Notice with ID " + nno + " not found."));
        updatedNoticeDTO.toUpdatedEntity(notice);
        return toDTO(notice);
    }

    // 엔티티 -> DTO 변환
    private NoticeDTO toDTO(Notice notice) {
        return NoticeDTO.builder()
                .nno(notice.getNno())
                .title(notice.getTitle())
                .content(notice.getContent())
                .writer(notice.getWriter())
                .isPinned(notice.getIsPinned())
                .build();
    }
}
