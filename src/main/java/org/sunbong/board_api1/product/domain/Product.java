package org.sunbong.board_api1.product.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"attachFiles","tags"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    private String name;

    private int price;

    @ElementCollection
    @Builder.Default
    private Set<AttachFile> attachFiles=new HashSet<>();

    @ElementCollection
    @Builder.Default
    @BatchSize(size = 50)
    private Set<String> tags=new HashSet<>();

    public void addFile(String filename) {
        attachFiles.add(new AttachFile(attachFiles.size(), filename));
    }

    public void clearFile() {
        attachFiles.clear();
    }

    public void addTag(String tag) {
        tags.add(tag);
    }
    public void clear() {
        tags.clear();
    }
}
