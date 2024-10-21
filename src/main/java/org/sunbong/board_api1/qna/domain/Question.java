package org.sunbong.board_api1.qna.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
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

    @ElementCollection
    @Builder.Default
    private Set<AttachFile> attachFiles = new HashSet<>();

    public void addFile(String filename) {
        attachFiles.add(new AttachFile(attachFiles.size(), filename));
    }

    public void clearFiles() {
        attachFiles.clear();
    }
}
