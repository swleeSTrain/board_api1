package org.sunbong.board_api1.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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

    private int priority; // 공지사항 중요도

    @Builder.Default
    private Boolean isPinned = Boolean.FALSE; // 공지사항 고정 여부

    private String writer;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @Builder.Default
    private NoticeStatus status = NoticeStatus.PUBLISHED; // 기본 상태는 PUBLISHED로 설정

    private List<String> attachDocuments; // 기존 첨부파일 목록을 문자열 리스트로 처리

    private List<MultipartFile> files; // 새로 업로드할 파일 리스트
}
