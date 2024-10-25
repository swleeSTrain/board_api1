package org.sunbong.board_api1.notice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.board_api1.notice.domain.Notice;
import org.sunbong.board_api1.notice.dto.NoticeDTO;
import org.sunbong.board_api1.notice.repository.NoticeRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    //등록
    public NoticeDTO save(NoticeDTO noticeDTO) throws Exception {

        Notice notice = toEntity(noticeDTO);

        Notice saveNotice = noticeRepository.save(notice);

        return toDTO(saveNotice);
    }

    //조회
    public NoticeDTO findById(Long nno) throws Exception {

        Notice notice = noticeRepository.findById(nno)
                .orElseThrow(() -> new Exception("Notice Not Found"));

        return toDTO(notice);
    }

    //삭제
    public void deleteById(Long nno) throws Exception {

        Notice notice = noticeRepository.findById(nno)
                .orElseThrow(() -> new Exception("Notice Not Found"));

        noticeRepository.delete(notice);
    }

    // DTO -> 엔티티
    private Notice toEntity(NoticeDTO noticeDTO) throws Exception {
        return Notice.builder()
                .title(noticeDTO.getTitle())
                .content(noticeDTO.getContent())
                .writer(noticeDTO.getWriter())
                .isPinned(noticeDTO.isPinned())
                .build();
    }

    // 엔티티 -> DTO
    private NoticeDTO toDTO(Notice notice) {
        return NoticeDTO.builder()
                .nno(notice.getNno())
                .title(notice.getTitle())
                .content(notice.getContent())
                .writer(notice.getWriter())
                .isPinned(notice.isPinned())
                .build();
    }

}
