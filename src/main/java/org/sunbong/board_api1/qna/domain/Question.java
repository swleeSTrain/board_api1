package org.sunbong.board_api1.qna.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.sunbong.board_api1.common.domain.BaseEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"attachFiles", "tags"})
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qno;

    private String title;

    private String content;

    private String writer;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    @BatchSize(size = 20)
    private Set<String> tags = new HashSet<>();

    @ElementCollection
    @Builder.Default
    @BatchSize(size = 20)
    private Set<QuestionAttachFile> attachFiles = new HashSet<>();

    public void addFile(String filename) {
        attachFiles.add(new QuestionAttachFile(attachFiles.size(), filename));
    }

    public void clearFiles() {
        attachFiles.clear();
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void clearTag() {
        tags.clear();
    }

    public void editQuestion(String title, String content, String writer, Set<String> tags) {

        this.title = title;
        this.content = content;
        this.writer = writer;

        // 태그 초기화 후 새로 추가
        this.clearTag();
        if (tags != null) {
            tags.forEach(this::addTag);
        }
    }
}
