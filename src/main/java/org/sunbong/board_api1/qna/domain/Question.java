package org.sunbong.board_api1.qna.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"attachFiles"})
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qno;

    private String title;

    private String content;

    private String writer;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @ElementCollection
    @Builder.Default
    @BatchSize(size = 50)
    private Set<String> tags = new HashSet<>();

    @ElementCollection
    @Builder.Default
    private Set<AttachFile> attachFiles = new HashSet<>();
    //dfd

    public void addFile(String filename) {
        attachFiles.add(new AttachFile(attachFiles.size(), filename));
    }

    public void clearFiles() {
        attachFiles.clear();
    }

    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now(); // 등록 시 현재 시간
        modifiedDate = LocalDateTime.now();  // 등록 시 현재 시간
    }

    @PreUpdate
    public void preUpdate() {
        modifiedDate = LocalDateTime.now(); // 수정 시 현재 시간
    }

    public void addTag(String tag) {
        tags.add(tag);
    }
    public void clear() {
        tags.clear();
    }
}
