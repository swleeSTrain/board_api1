package org.sunbong.board_api1.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.sunbong.board_api1.notice.domain.NoticeStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {
    private Long noticeNo;

    private String noticeTitle;

    private String noticeContent;

    private LocalDateTime startDate; // 게시 시작 시간

    private LocalDateTime endDate; // 게시 종료 시간

    private int priority; // 공지사항 중요도

    private Boolean isPinned = Boolean.FALSE; // 공지사항 고정 여부

    private String writer;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private NoticeStatus status;

    private List<String> attachDocuments; // 첨부파일 목록을 문자열 리스트로 처리

    private List<MultipartFile> files;
}
