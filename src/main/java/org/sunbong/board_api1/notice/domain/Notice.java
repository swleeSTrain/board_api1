package org.sunbong.board_api1.notice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder(toBuilder = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString (exclude = {"attachDocuments"})
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeNo;

    @Builder.Default
    private int priority = 0; // 공지사항 중요도

    @Builder.Default
    private Boolean isPinned = Boolean.FALSE; // 공지사항 고정 여부

    private String noticeTitle;

    private String noticeContent;

    private LocalDateTime startDate; // 게시 시작

    private LocalDateTime endDate; // 게시 끝

    private String writer;

    @Builder.Default
    private NoticeStatus status = NoticeStatus.PUBLISHED; // 공지사항 상태

    @CreationTimestamp
    private LocalDateTime createTime;  // 작성 시간

    @UpdateTimestamp
    private LocalDateTime updateTime;  // 수정 시간

    @ElementCollection
    @Builder.Default
    private Set<AttachedDocument> attachDocuments = new HashSet<>();

    public void addFile(String filename) {
        attachDocuments.add(new AttachedDocument(attachDocuments.size(), filename));
    }

    public void clearFile() {
        attachDocuments.clear();
    }

    public boolean isActiveNow() {
        if (startDate == null || endDate == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(startDate) && LocalDateTime.now().isBefore(endDate);
    }

    // 공지사항 고정 여부
    public void pinNotice() {
        this.isPinned = Boolean.TRUE;  // Boolean.TRUE 사용
    }

    public void unpinNotice() {
        this.isPinned = Boolean.FALSE;  // Boolean.FALSE 사용
    }

    // 상태를 시간에 맞게 자동으로 변경하는 메서드
    public void updateStatusBasedOnTime() {
        if (isActiveNow()) {
            this.status = NoticeStatus.PUBLISHED;
        } else if (LocalDateTime.now().isAfter(endDate)) {
            this.status = NoticeStatus.ARCHIVED;
        } else {
            this.status = NoticeStatus.DRAFT;
        }
    }
}
