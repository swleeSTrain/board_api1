package org.sunbong.board_api1.qna.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "question")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ano;

    private String content;

    private String writer;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now(); // 등록 시 현재 시간
        modifiedDate = LocalDateTime.now();  // 등록 시 현재 시간
    }

    @PreUpdate
    public void preUpdate() {
        modifiedDate = LocalDateTime.now(); // 수정 시 현재 시간
    }

}
