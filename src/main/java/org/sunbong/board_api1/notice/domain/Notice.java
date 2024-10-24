package org.sunbong.board_api1.notice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
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

    private String writer;

    @Builder.Default
    private NoticeStatus status = NoticeStatus.PUBLISHED; // 공지사항 상태

    @CreationTimestamp
    private LocalDateTime createTime;  // 작성 시간

    @UpdateTimestamp
    private LocalDateTime updateTime;  // 수정 시간

    @ElementCollection
    @Builder.Default
    @BatchSize(size = 100)
    private Set<AttachedDocument> attachDocuments = new HashSet<>();

    // 파일 추가 메서드
    public void addFile(String filename) {
        attachDocuments.add(new AttachedDocument(attachDocuments.size(), filename));
    }

    // 기존 파일을 유지하면서 새 파일 추가
    public void addFiles(Set<AttachedDocument> newFiles) {
        attachDocuments.addAll(newFiles);
    }

    // 파일 삭제 메서드 (선택적으로 특정 파일을 삭제할 수 있도록)
    public void removeFile(String filename) {
        attachDocuments.removeIf(document -> document.getFileName().equals(filename));
    }

    // 파일 전체 삭제 메서드
    public void clearFiles() {
        attachDocuments.clear();
    }


    // 공지사항 고정 여부
    public void pinNotice() {
        this.isPinned = Boolean.TRUE;  // Boolean.TRUE 사용
    }

    public void unpinNotice() {
        this.isPinned = Boolean.FALSE;  // Boolean.FALSE 사용
    }

}
