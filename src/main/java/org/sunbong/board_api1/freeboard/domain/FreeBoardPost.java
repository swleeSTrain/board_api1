package org.sunbong.board_api1.freeboard.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
public class FreeBoardPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String author;

    private String content;

    @Builder.Default
    private boolean deleted = false;

    @ElementCollection
    @Builder.Default
    private Set<BoardAttachFile> boardAttachFiles = new HashSet<>();



    public void clearAttachFiles() { boardAttachFiles.clear();}
}
