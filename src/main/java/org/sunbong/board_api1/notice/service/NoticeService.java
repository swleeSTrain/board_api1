package org.sunbong.board_api1.notice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.common.exception.CommonExceptions;
import org.sunbong.board_api1.notice.domain.Notice;
import org.sunbong.board_api1.notice.domain.NoticeStatus;
import org.sunbong.board_api1.notice.dto.NoticeDTO;
import org.sunbong.board_api1.notice.dto.NoticeListDTO;
import org.sunbong.board_api1.notice.repository.NoticeRepository;
import org.sunbong.board_api1.notice.domain.AttachedDocument;

import java.io.IOException;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final FileService fileService; // 파일 서비스 추가

    public PageResponseDTO<NoticeListDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("Fetching notice list with pagination");

        if (pageRequestDTO.getPage() < 0) {
            throw CommonExceptions.LIST_ERROR.get();
        }
        return noticeRepository.noticeList(pageRequestDTO);
    }

    // 등록(Create)
    public NoticeDTO save(NoticeDTO noticeDTO) throws IOException {
        Notice notice = Notice.builder()
                .noticeTitle(noticeDTO.getNoticeTitle())
                .noticeContent(noticeDTO.getNoticeContent())
                .priority(noticeDTO.getPriority())
                .isPinned(Boolean.TRUE.equals(noticeDTO.getIsPinned()))
                .writer(noticeDTO.getWriter())
                .build();

        // 파일 업로드 처리
        if (noticeDTO.getFiles() != null && !noticeDTO.getFiles().isEmpty()) {
            for (MultipartFile file : noticeDTO.getFiles()) {
                String fileName = fileService.saveFile(file); // 파일 저장 후 파일 이름 가져오기
                notice.addFile(fileName); // Notice 엔티티에 파일 추가
            }
        }

        Notice savedNotice = noticeRepository.save(notice);
        return toDTO(savedNotice);
    }

    public NoticeDTO update(Long noticeNo, NoticeDTO noticeDTO) throws IOException {
        Notice existingNotice = noticeRepository.findById(noticeNo)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found with ID: " + noticeNo));

        Notice updatedNotice = existingNotice.toBuilder()
                .noticeTitle(noticeDTO.getNoticeTitle() != null ? noticeDTO.getNoticeTitle() : existingNotice.getNoticeTitle())
                .noticeContent(noticeDTO.getNoticeContent() != null ? noticeDTO.getNoticeContent() : existingNotice.getNoticeContent())
                .priority(noticeDTO.getPriority() != 0 ? noticeDTO.getPriority() : existingNotice.getPriority())
                .isPinned(noticeDTO.getIsPinned() != null ? noticeDTO.getIsPinned() : existingNotice.getIsPinned())
                .status(NoticeStatus.PUBLISHED)
                .build();

        // 파일 처리 로직
        if (noticeDTO.getFiles() != null) {
            // 새 파일이 있는 경우
            if (!noticeDTO.getFiles().isEmpty()) {
                // 기존 파일 유지하면서 새 파일 추가
                for (MultipartFile file : noticeDTO.getFiles()) {
                    String fileName = fileService.saveFile(file); // 파일 저장
                    updatedNotice.addFile(fileName); // 공지사항에 파일 추가
                }
            }
        }

        // 업데이트된 공지사항 저장
        noticeRepository.save(updatedNotice);

        return toDTO(updatedNotice);
    }

    // 조회
    public NoticeDTO findById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found with ID: " + id));

        return toDTO(notice);
    }

    // 삭제
    public void delete(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found with ID: " + id));

        noticeRepository.delete(notice);
    }

    // 엔티티에서 DTO로 변환하는 메서드
    private NoticeDTO toDTO(Notice notice) {
        return NoticeDTO.builder()
                .noticeNo(notice.getNoticeNo())
                .noticeTitle(notice.getNoticeTitle())
                .noticeContent(notice.getNoticeContent())
                .priority(notice.getPriority())
                .isPinned(Boolean.TRUE.equals(notice.getIsPinned()))
                .writer(notice.getWriter())
                .createTime(notice.getCreateTime())
                .updateTime(notice.getUpdateTime())
                .status(notice.getStatus())
                .attachDocuments(notice.getAttachDocuments().stream()
                        .map(AttachedDocument::getFileName)
                        .collect(Collectors.toList()))
                .build();
    }
}
