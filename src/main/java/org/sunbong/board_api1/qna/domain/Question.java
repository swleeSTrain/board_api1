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

    @ElementCollection
    @Builder.Default
    @BatchSize(size = 50)
    private Set<String> tags = new HashSet<>();

    @ElementCollection
    @Builder.Default
    @BatchSize(size = 50)
    private Set<AttachFileQna> attachFiles = new HashSet<>();

    public void addFile(String filename) {
        attachFiles.add(new AttachFileQna(attachFiles.size(), filename));
    }

    public void clearFiles() {
        attachFiles.clear();
    }

    public void addTag(String tag) {
        tags.add(tag);
    }
    public void clear() {
        tags.clear();
    }
}
