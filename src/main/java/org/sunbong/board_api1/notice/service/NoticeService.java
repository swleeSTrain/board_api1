package org.sunbong.board_api1.notice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.common.exception.CommonExceptions;
import org.sunbong.board_api1.notice.domain.Notice;
import org.sunbong.board_api1.notice.dto.NoticeDTO;
import org.sunbong.board_api1.notice.dto.NoticeListDTO;
import org.sunbong.board_api1.notice.repository.NoticeRepository;
import org.sunbong.board_api1.notice.domain.AttachedDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    // 파일 저장 경로를 설정 파일에서 읽어옴
    @Value("${org.sunbong.upload.path}")
    private String uploadDir;


    public PageResponseDTO<NoticeListDTO> list(PageRequestDTO pageRequestDTO) {

        log.info("Fetching notice list with pagination");

        if(pageRequestDTO.getPage() < 0) {
            throw CommonExceptions.LIST_ERROR.get();
        }
        // NoticeSearch 인터페이스를 통해 공지사항 목록을 조회
        PageResponseDTO<NoticeListDTO> result = noticeRepository.noticeList(pageRequestDTO);

        return result;
    }

    // 등록(Create)
    public NoticeDTO save(NoticeDTO noticeDTO) throws IOException {
        // Notice 엔티티로 변환
        Notice notice = Notice.builder()
                .noticeTitle(noticeDTO.getNoticeTitle())
                .noticeContent(noticeDTO.getNoticeContent())
                .startDate(noticeDTO.getStartDate())
                .endDate(noticeDTO.getEndDate())
                .priority(noticeDTO.getPriority())
                .isPinned(noticeDTO.isPinned())
                .writer(noticeDTO.getWriter())
                .build();

        // 상태 업데이트
        notice.updateStatusBasedOnTime();  // 상태 업데이트 로직 호출

        // 파일 업로드 처리
        if (noticeDTO.getFiles() != null && !noticeDTO.getFiles().isEmpty()) {
            for (MultipartFile file : noticeDTO.getFiles()) {
                String fileName = saveFile(file); // 파일 저장 후 파일 이름 가져오기
                notice.addFile(fileName); // Notice 엔티티에 파일 추가
            }
        }

        // 저장
        Notice savedNotice = noticeRepository.save(notice);

        // 저장된 엔티티를 DTO로 변환하여 반환
        return toDTO(savedNotice);
    }

    // 파일 저장 메서드
    private String saveFile(MultipartFile file) throws IOException {
        try {
            // 파일 저장 경로 설정
            Path uploadPath = Paths.get(uploadDir);

            // 디렉토리가 없으면 생성
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // UUID로 고유한 파일 이름을 생성
            String originalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "_" + originalFileName; // UUID 추가
            Path filePath = uploadPath.resolve(fileName);

            // 파일 저장
            Files.copy(file.getInputStream(), filePath);

            return fileName; // 고유한 파일 이름 반환
        } catch (IOException e) {
            log.error("파일 저장 중 오류 발생: " + file.getOriginalFilename(), e);
            throw new IOException("파일 저장 실패", e); // 예외 처리
        }
    }

    // 엔티티에서 DTO로 변환하는 메서드
    private NoticeDTO toDTO(Notice notice) {
        return NoticeDTO.builder()
                .noticeNo(notice.getNoticeNo())
                .noticeTitle(notice.getNoticeTitle())
                .noticeContent(notice.getNoticeContent())
                .startDate(notice.getStartDate())
                .endDate(notice.getEndDate())
                .priority(notice.getPriority())
                .isPinned(notice.isPinned())
                .writer(notice.getWriter())
                .createTime(notice.getCreateTime())
                .updateTime(notice.getUpdateTime())
                .status(notice.getStatus())
                .attachDocuments(notice.getAttachDocuments().stream()
                        .map(AttachedDocument::getFileName)
                        .collect(Collectors.toList())) // 첨부 파일 리스트
                .build();
    }
}
