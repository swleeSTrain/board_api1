package org.sunbong.board_api1.board.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString

public class BoardPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;//제목

    private String author;//작성자

    private String content;//내용
    @CreationTimestamp
    private LocalDateTime createTime;//생성날짜
    @UpdateTimestamp
    private LocalDateTime updateTime;//수정날짜
    @Builder.Default
    private boolean delflag = false;


    @ElementCollection
    @Builder.Default
    private Set<BoardAttachFile> boardAttachFiles = new HashSet<>();

    public void addBoardAttachFile(String fileName) {
        boardAttachFiles.add(new BoardAttachFile(boardAttachFiles.size(),fileName) );
    }

    public void clearBoardAttachFiles() { boardAttachFiles.clear();}
}
